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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.itwillbs.ilkwangtech.schedule.entity.ScheduleFile;
import com.itwillbs.ilkwangtech.schedule.repository.ScheduleFileRepository;
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
    private final ScheduleFileRepository scheduleFileRepository;
    
    // application.properties의 설정값 (/usr/local/tomcat/upload)
    @Value("${file.uploadBaseLocation}") 
    private String baseDir;
    
    /**
     * [핵심 로직] OS 환경을 감지하여 실제 저장 경로를 반환합니다.
     * - 윈도우: C:/usr/local/tomcat/upload/
     * - 리눅스: /usr/local/tomcat/upload/
     */
    private String getRealUploadPath() {
        String path = baseDir;
        // 윈도우 환경이고 경로가 /로 시작하면 C: 드라이브를 붙임
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (path.startsWith("/")) {
                path = "C:" + path;
            }
        }
        // 경로 끝에 슬래시가 없으면 붙임
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path;
    }
    
    // 일정 리스트 조회
    @Transactional(readOnly = true)
    public Page<ScheduleDTO> getScheduleList(Long loginMemberId, ScheduleSearchDTO params, Pageable pageable) {
        log.info("getScheduleList() 실행!");
        
        Member loginMember = memberRepository.findById(loginMemberId)
                                             .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        
        int myDeptId = loginMember.getDepartment();
        
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        
        if (params.getStartDate() == null) {
            startDateTime = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();            
            endDateTime = LocalDate.now().plusYears(1).with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX);
        } else {
            startDateTime = params.getStartDate().atStartOfDay(); 
            endDateTime = params.getEndDate().atTime(LocalTime.MAX);
        }
        
        String dbKeyword = params.getKeyword();
        if ("type".equals(params.getSearchType()) && dbKeyword != null) {
            String k = dbKeyword.trim();
            if ("회사".equals(k)) dbKeyword = "COMPANY";
            else if ("개인".equals(k)) dbKeyword = "PERSONAL";
            else if ("연차".equals(k)) dbKeyword = "LEAVE";
            else if ("팀".equals(k)) dbKeyword = "TEAM";
            else if ("특정".equals(k)) dbKeyword = "SPECIFIC";
        }

        Page<Schedule> pageResult = scheduleRepository.findWithSharing(
                loginMemberId,
                myDeptId,
                startDateTime,
                endDateTime,
                dbKeyword,
                params.getSearchType(),
                pageable 
        );

        return pageResult.map(ScheduleDTO::fromEntity);
    }

    // 일정 등록
    @Transactional
    public void registSchedule(ScheduleDTO scheduleDto, Long id) {
        log.info("registSchedule() 실행!");
        
        Member writer = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        Schedule schedule = scheduleDto.toEntity(writer);

        // 썸네일 저장
        if (scheduleDto.getThumbnail() != null && !scheduleDto.getThumbnail().isEmpty()) {
            String thumbName = saveFile(scheduleDto.getThumbnail()); 
            schedule.setThumbnailPath(thumbName);
        }

        // 일반 파일 저장
        if (scheduleDto.getDataFiles() != null && !scheduleDto.getDataFiles().isEmpty()) {
            for (MultipartFile mf : scheduleDto.getDataFiles()) {
                if (mf.isEmpty()) continue;
                
                String fileName = saveFile(mf);
                
                ScheduleFile fileEntity = ScheduleFile.builder()
                        .originalFileName(mf.getOriginalFilename())
                        .savedFileName(fileName)
                        .fileSize(mf.getSize())
                        .schedule(schedule)
                        .build();
                
                schedule.addFile(fileEntity);
            }
        }

        if ("TEAM".equals(scheduleDto.getType()) && scheduleDto.getSharedDeptIds() != null) {
            List<Department> departments = departmentRepository.findAllById(scheduleDto.getSharedDeptIds());
            schedule.setSharedDepartments(new HashSet<>(departments));
        } else if ("SPECIFIC".equals(scheduleDto.getType()) && scheduleDto.getSharedMemberIds() != null) {
            List<Member> members = memberRepository.findAllById(scheduleDto.getSharedMemberIds());
            schedule.setSharedMembers(new HashSet<>(members));
        }

        scheduleRepository.save(schedule);
        log.info("일정 등록 완료: ID={}", schedule.getId());
    }
    
    // [수정된 부분] 파일 실제 저장 로직
    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) return null;

        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String savedFileName = uuid + "_" + originalFileName;

        // ★ OS에 맞는 실제 경로를 가져와서 저장
        String realPath = getRealUploadPath() + savedFileName;
        File dest = new File(realPath);
        
        // 상위 디렉토리(C:/usr/local/tomcat/upload)가 없으면 생성
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
            return savedFileName;
        } catch (IOException e) {
            log.error("파일 업로드 실패: " + realPath, e); // 경로 확인용 로그
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    public List<Department> searchTeams(String keyword) {
        return departmentRepository.findByDepartmentNameContaining(keyword);
    }

    public List<Member> searchMembers(String keyword) {
        return memberRepository.findByNameContaining(keyword);
    }

    @Transactional(readOnly = true)
    public ScheduleDTO getScheduleDetails(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));
        return ScheduleDTO.fromEntity(schedule);
    }

    @Transactional
    public void updateSchedule(ScheduleDTO dto, Long memberId) {
        Schedule schedule = scheduleRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));

        if (!schedule.getWriter().getId().equals(memberId)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        
        String updatedThumbnailPath = schedule.getThumbnailPath();
        if (dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()) {
            updatedThumbnailPath = saveFile(dto.getThumbnail());
        }

        String updatedAttachmentFile = schedule.getAttachmentFile();
        if (dto.getAttachment() != null && !dto.getAttachment().isEmpty()) {
            updatedAttachmentFile = saveFile(dto.getAttachment());
        }

        schedule.updateSchedule(
            dto.getTitle(), dto.getContent(), dto.getType(),
            dto.getStartDate(), dto.getEndDate(),
            updatedAttachmentFile, updatedThumbnailPath
        );
        
        if (dto.getDataFiles() != null && !dto.getDataFiles().isEmpty()) {
            for (MultipartFile mf : dto.getDataFiles()) {
                if (mf.isEmpty()) continue;
                String fileName = saveFile(mf);
                ScheduleFile fileEntity = ScheduleFile.builder()
                        .originalFileName(mf.getOriginalFilename())
                        .savedFileName(fileName)
                        .fileSize(mf.getSize())
                        .schedule(schedule)
                        .build();
                schedule.addFile(fileEntity);
            }
        }
        
        schedule.getSharedDepartments().clear();
        schedule.getSharedMembers().clear();

        if ("TEAM".equals(dto.getType()) && dto.getSharedDeptIds() != null) {
            List<Department> departments = departmentRepository.findAllById(dto.getSharedDeptIds());
            schedule.setSharedDepartments(new HashSet<>(departments));
        } else if ("SPECIFIC".equals(dto.getType()) && dto.getSharedMemberIds() != null) {
            List<Member> members = memberRepository.findAllById(dto.getSharedMemberIds());
            schedule.setSharedMembers(new HashSet<>(members));
        }
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));

        if (!schedule.getWriter().getId().equals(memberId)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        
        schedule.getSharedDepartments().clear();
        schedule.getSharedMembers().clear();
        scheduleRepository.flush();
        
        // [수정된 부분] 삭제 시에도 OS 경로 반영
        if (schedule.getAttachmentFile() != null) {
            File file = new File(getRealUploadPath() + schedule.getAttachmentFile());
            if (file.exists()) {
                if (file.delete()) log.info("첨부 파일 삭제 성공");
                else log.warn("첨부 파일 삭제 실패");
            }
        }
        scheduleRepository.delete(schedule);
    }
    
    @Transactional(readOnly = true)
    public ScheduleFile getScheduleFile(Long fileId) {
        return scheduleFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다."));
    }
    
    @Transactional
    public void deleteAttachedFile(Long fileId, Long memberId) {
        ScheduleFile fileEntity = scheduleFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

        if (!fileEntity.getSchedule().getWriter().getId().equals(memberId)) {
            throw new SecurityException("파일 삭제 권한이 없습니다.");
        }

        // [수정된 부분] 개별 파일 삭제 시에도 OS 경로 반영
        File file = new File(getRealUploadPath() + fileEntity.getSavedFileName());
        if (file.exists()) {
            if (file.delete()) log.info("파일 삭제 성공");
            else log.warn("파일 삭제 실패");
        }
        scheduleFileRepository.delete(fileEntity);
    }
}