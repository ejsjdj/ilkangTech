package com.itwillbs.ilkwangtech.schedule.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.schedule.dto.ScheduleDTO;
import com.itwillbs.ilkwangtech.schedule.dto.ScheduleSearchDTO;
import com.itwillbs.ilkwangtech.schedule.entity.Schedule;
import com.itwillbs.ilkwangtech.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ScheduleService {
	
	private final ScheduleRepository scheduleRepository;
	private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    
    @Value("${file.upload.path:C:/upload/}") 
    private String uploadDir;
	
	// 일정 리스트 조회
	// [변경] 파라미터가 int page -> Pageable pageable 로 바뀝니다.
	@Transactional(readOnly = true)
	public Page<ScheduleDTO> getScheduleList(Long loginMemberId, ScheduleSearchDTO params, Pageable pageable) {
	    log.info("getScheduleList() 실행!");
	    
	    Member loginMember = memberRepository.findById(loginMemberId)
                                             .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
	    
        int myDeptId = loginMember.getDepartment();
		
	    LocalDateTime startDateTime;
	    LocalDateTime endDateTime;
	    
	    // 1. 날짜 기본값 설정 
	    if (params.getStartDate() == null) {
	        // 시작: 올해 1월 1일
	        startDateTime = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();	        
	        // 종료: 내년 12월 31일 (미래 일정까지 보이도록 넉넉하게)
	        endDateTime = LocalDate.now().plusYears(1).with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX);
	    } else {
	        startDateTime = params.getStartDate().atStartOfDay(); 
	        endDateTime = params.getEndDate().atTime(LocalTime.MAX);
	    }
	    
	    // 검색 카테고리 종류(공유 범위) 부분 추가
	    String dbKeyword = params.getKeyword();
	    
	    if ("type".equals(params.getSearchType()) && dbKeyword != null) {
	        String k = dbKeyword.trim();
	        if ("회사".equals(k)) dbKeyword = "COMPANY";
	        else if ("개인".equals(k)) dbKeyword = "PERSONAL";
	        else if ("연차".equals(k)) dbKeyword = "LEAVE";
            else if ("팀".equals(k)) dbKeyword = "TEAM";
            else if ("특정".equals(k)) dbKeyword = "SPECIFIC";
	    }

	    // Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("startDate").ascending());
	    
	    // 2. 리포지토리 호출
	    Page<Schedule> pageResult = scheduleRepository.findWithSharing(
	            loginMemberId,
	            myDeptId,
	            startDateTime,
	            endDateTime,
	            dbKeyword,
	            params.getSearchType(), // [추가] DTO에서 꺼내서 전달
	            pageable 
	    );

	    // 3. 변환 후 반환
	    log.info("getScheduleList() 종료!");
	    return pageResult.map(ScheduleDTO::fromEntity);
	}

	// 일정 등록
	@Transactional
	public void registSchedule(ScheduleDTO scheduleDto, Long id) {
		log.info("registSchedule() 실행!");
		
		// 1. 작성자(Member) 조회
        Member writer = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 파일 업로드 처리
        String savedFileName = null;
        if (scheduleDto.getAttachment() != null && !scheduleDto.getAttachment().isEmpty()) {
            savedFileName = saveFile(scheduleDto.getAttachment());
            scheduleDto.setAttachmentFile(savedFileName); // DTO에 저장된 파일명 세팅
        }

        // 3. DTO -> Entity 변환 (기본 정보 및 작성자 설정)
        // ScheduleDTO.java의 toEntity 메서드를 활용
        Schedule schedule = scheduleDto.toEntity(writer);

        // 4. 공유 범위에 따른 연관관계 설정 (팀/사원)
        // 주의: Schedule Entity에 @ManyToMany 필드(sharedDepartments, sharedMembers)가 있어야 함
        
        // [팀 공유]
        if ("TEAM".equals(scheduleDto.getType()) && scheduleDto.getSharedDeptIds() != null && !scheduleDto.getSharedDeptIds().isEmpty()) {
            List<Department> departments = departmentRepository.findAllById(scheduleDto.getSharedDeptIds());
            // Schedule 엔티티에 세터나 편의 메서드가 필요함 (예: setSharedDepartments)
            schedule.setSharedDepartments(new HashSet<>(departments));
        }
        
        // [특정 사원 공유]
        else if ("SPECIFIC".equals(scheduleDto.getType()) && scheduleDto.getSharedMemberIds() != null && !scheduleDto.getSharedMemberIds().isEmpty()) {
            List<Member> members = memberRepository.findAllById(scheduleDto.getSharedMemberIds());
            // Schedule 엔티티에 세터나 편의 메서드가 필요함 (예: setSharedMembers)
            schedule.setSharedMembers(new HashSet<>(members));
        }

        // 5. 최종 저장
        scheduleRepository.save(schedule);
        log.info("일정 등록 완료: ID={}, Title={}", schedule.getId(), schedule.getTitle());
        log.info("registSchedule() 종료!");
	}
	
	// 파일 저장 로직 (내부 헬퍼 메서드)
    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) return null;

        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        // 저장될 파일명: "UUID_원본이름"
        String savedFileName = uuid + "_" + originalFileName;

        File dest = new File(uploadDir + savedFileName);
        
        // 디렉토리가 없으면 생성
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest); // 실제 파일 저장
            return savedFileName;
        } catch (IOException e) {
            log.error("파일 업로드 실패: ", e);
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    // 부서(팀) 검색 기능 구현
	public List<Department> searchTeams(String keyword) {
		log.info("searchTeams() 실행!");
		log.info("searchTeams() 종료!");
		return departmentRepository.findByDepartmentNameContaining(keyword);
	}

	// 사원 검색 기능 구현
	public List<Member> searchMembers(String keyword) {
		log.info("searchMembers() 실행!");
		log.info("searchMembers() 종료!");
		return memberRepository.findByNameContaining(keyword);
	}

	// 일정 상세 조회
	@Transactional(readOnly = true)
    public ScheduleDTO getScheduleDetails(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));
        
        return ScheduleDTO.fromEntity(schedule); // Entity -> DTO 변환 메서드 활용
    }

	// 일정 수정 처리
	@Transactional
    public void updateSchedule(ScheduleDTO dto, Long memberId) {
        Schedule schedule = scheduleRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));

        // 작성자 본인 확인 (Backend 검증)
        if (!schedule.getWriter().getId().equals(memberId)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        // 파일 수정 로직 (새 파일이 올라왔을 경우)
        String updatedFileName = schedule.getAttachmentFile(); // 기존 파일 유지 기본값
        if (dto.getAttachment() != null && !dto.getAttachment().isEmpty()) {
            // (선택사항) 기존 파일 삭제 로직 추가 가능
            updatedFileName = saveFile(dto.getAttachment()); // 새 파일 저장
        }

        // 기본 정보 업데이트 (Dirty Checking 활용)
        // Entity에 작성해둔 updateSchedule 편의 메서드 활용
        schedule.updateSchedule(
            dto.getTitle(),
            dto.getContent(),
            dto.getType(),
            dto.getStartDate(),
            dto.getEndDate(),
            updatedFileName
        );
        
        // 공유 범위 업데이트 로직 (기존 관계 초기화 후 재설정)
        // 1. 기존 공유 삭제
        schedule.getSharedDepartments().clear();
        schedule.getSharedMembers().clear();

        // 2. 새 공유 설정 (등록 로직 재사용)
        if ("TEAM".equals(dto.getType()) && dto.getSharedDeptIds() != null) {
            List<Department> departments = departmentRepository.findAllById(dto.getSharedDeptIds());
            schedule.setSharedDepartments(new HashSet<>(departments));
        } else if ("SPECIFIC".equals(dto.getType()) && dto.getSharedMemberIds() != null) {
            List<Member> members = memberRepository.findAllById(dto.getSharedMemberIds());
            schedule.setSharedMembers(new HashSet<>(members));
        }
    }

	// 일정 삭제 처리
	@Transactional
    public void deleteSchedule(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));

        // 1. 권한 체크
        if (!schedule.getWriter().getId().equals(memberId)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        
        // 2. 연관 관계 끊기 (외래 키 제약 조건 방지)
        schedule.getSharedDepartments().clear();
        schedule.getSharedMembers().clear();
        
        // 2-1. 해당 schedule_id 데이터를 삭제하도록 플러시
        scheduleRepository.flush();
        
        // 3. 실제 파일 삭제 (파일이 존재할 경우)
        if (schedule.getAttachmentFile() != null) {
            File file = new File(uploadDir + schedule.getAttachmentFile());
            if (file.exists()) {
                if (file.delete()) {
                    log.info("첨부 파일 삭제 성공: " + schedule.getAttachmentFile());
                } else {
                    log.warn("첨부 파일 삭제 실패(파일 시스템 권한 등 문제 가능성): " + schedule.getAttachmentFile());
                    // 파일 삭제 실패가 DB 롤백을 유발하지 않게 하려면 예외를 던지지 않고 로그만 남깁니다.
                }
            }
        }
        
        // (선택사항) 로컬 파일 삭제 로직 추가 가능
        scheduleRepository.delete(schedule);
    }


}
