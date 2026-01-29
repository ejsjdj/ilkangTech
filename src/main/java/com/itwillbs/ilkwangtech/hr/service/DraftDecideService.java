package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.entity.DraftApproveStatusEntity;
import com.itwillbs.ilkwangtech.hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DraftDecideService {

    private final DraftApproveStatusRepository draftApproveStatusRepository;
    private final DraftRepository draftRepository;

    public DraftDecideService(DraftApproveStatusRepository draftApproveStatusRepository, DraftRepository draftRepository){
        this.draftApproveStatusRepository = draftApproveStatusRepository;
        this.draftRepository = draftRepository;
    }

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
        }
    }
}
