package com.itwillbs.ilkwangtech.hr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.itwillbs.ilkwangtech.hr.entity.LeaveEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.hr.entity.DraftApproveStatusEntity;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.schedule.dto.ScheduleDTO;
import com.itwillbs.ilkwangtech.schedule.service.ScheduleService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DraftDecideService {

    private final DraftApproveStatusRepository draftApproveStatusRepository;
    private final DraftRepository draftRepository;
    
    // [추가] 일정 등록과 팀장 정보 조회를 위해 서비스와 리포지토리 주입
    private final ScheduleService scheduleService;
    private final MemberRepository memberRepository;

    public DraftDecideService(DraftApproveStatusRepository draftApproveStatusRepository, DraftRepository draftRepository, 
    		                  ScheduleService scheduleService, MemberRepository memberRepository){
        this.draftApproveStatusRepository = draftApproveStatusRepository;
        this.draftRepository = draftRepository;
        this.scheduleService = scheduleService;
        this.memberRepository = memberRepository;
    }
    
    private static final List<Integer> MANAGER_POS_IDS = Arrays.asList(1, 2, 3);

    // 승인 반려
    @Transactional
    public void putApprovalDecide(long userId, long draftId, String decision){

        // 승인 또는 반려된 문서 체크
        DraftApproveStatusEntity statusEntity = draftApproveStatusRepository.findByMemberIdWithDraft(userId, draftId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문서를 찾을 수 없습니다"));

        if(!"대기".equals(statusEntity.getStatus())){
            throw new IllegalStateException("이미 승인 또는 반려된 문서입니다.");
        }

        // 결재 문서 존재 체크
        int updated = draftApproveStatusRepository.updateStatus(userId, draftId, decision);
        if(updated == 0) {
            throw new IllegalStateException("해당 문서를 찾을 수 없습니다");
        }

        // 최종 상태 업데이트
        List<DraftApproveStatusEntity> statusFinal = draftApproveStatusRepository.findByDraftEntity_DraftId(draftId);

        boolean hasRejected = statusFinal.stream()
                .anyMatch(s -> "반려".equals(s.getStatus()));

        boolean allApproved = statusFinal.stream()
                .allMatch(s -> "승인".equals(s.getStatus()));

        if(hasRejected) {
            draftRepository.updateFinalStatus(draftId, "반려");
        } else if (allApproved) {
            draftRepository.updateFinalStatus(draftId, "승인");
            
            // [추가] 최종 승인이 났을 때 캘린더 등록 로직 호출
            registerVacationToCalendar(userId, draftId);
        }
    }

	private void registerVacationToCalendar(long userId, long draftId) {
		// 결재 정보 조회
        DraftEntity draft = draftRepository.findById(draftId)
                .orElseThrow(() -> new IllegalArgumentException("문서 정보가 존재하지 않습니다."));

        // '휴가(PTO)' 또는 '반차(HDF)'인 경우에만 캘린더에 등록
        if ("PTO".equals(draft.getDraftType()) || "HDF".equals(draft.getDraftType())) {
            
            log.info("최종 승인된 연차 건 캘린더 등록 시작 - draftId: {}", draftId);

            ScheduleDTO scheduleDto = new ScheduleDTO();
            scheduleDto.setTitle("[연차] " + draft.getMember().getName()); // 제목: [연차] 홍길동
            scheduleDto.setContent(draft.getDraftContent()); // 내용: 결재 내용 복사

            // 잔여 휴가 갱신
//            LeaveEntity leave = draftRepository.findApprovedLeaveDraft(userId, draftId)
//                    .orElseThrow(() -> new IllegalArgumentException("휴가 정보를 찾을 수 없습니다"));
//            long usingLeave = draft.getDraftTotalDate(); // 사용할 휴가
//
//            long totalLeave = leave.getTotalLeave(); // 총 휴가
//            long usedLeave = leave.getUsedLeave(); // 사용한 휴가
//
//            long usedResult = usingLeave - usedLeave; // 사용한 휴가 + 사용할 휴가
//            long remainResult = totalLeave - usedResult; // 전체 휴가 - (사용한 휴가 + 사용할 휴가) = 잔여휴가
//
//            leave.setUsedLeave(usedResult);
//            leave.setRemainLeave(remainResult);


            // 날짜 변환: LocalDate -> LocalDateTime
            // 시작일 00:00, 종료일 23:59로 설정하여 캘린더에 꽉 차게 표시
            scheduleDto.setStartDate(draft.getDraftStartDate().atStartOfDay());
            scheduleDto.setEndDate(draft.getDraftEndDate().atTime(23, 59, 59));
            
            // 공유 타입: 특정 인원 공유 (본인 + 팀장)
            scheduleDto.setType("SPECIFIC"); 

            // 공유 대상자 리스트 생성
            List<Long> sharedMemberIds = new ArrayList<>();
            
            // 1) 기안자(본인) 추가
            sharedMemberIds.add(draft.getMember().getId());

            // 2) 소속 팀장 추가
            // Member 엔티티에서 department는 Integer 타입이므로 바로 가져옴
            Integer deptId = draft.getMember().getDepartment();
            
         // [핵심 변경] 리스트로 조회하여 반복문으로 추가
            List<Member> managers = memberRepository.findTeamManagers(deptId, MANAGER_POS_IDS);
            
            for (Member manager : managers) {
                // 본인이 간부인 경우 중복 추가 방지
                if (!manager.getId().equals(draft.getMember().getId())) {
                    sharedMemberIds.add(manager.getId());
                    log.info("캘린더 공유 대상 추가(간부): {} {}", manager.getPosition(), manager.getName());
                }
            }

            // DTO에 공유 대상 설정
            scheduleDto.setSharedMemberIds(sharedMemberIds);

            // 일정 등록 서비스 호출 (작성자는 기안자로 설정)
            scheduleService.registSchedule(scheduleDto, draft.getMember().getId());
        }
		
	}
}
