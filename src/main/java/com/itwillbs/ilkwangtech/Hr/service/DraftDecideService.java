package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.entity.DraftApproveStatusEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftApproveStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DraftDecideService {

    private final DraftApproveStatusRepository draftApproveStatusRepository;

    public DraftDecideService(DraftApproveStatusRepository draftApproveStatusRepository){
        this.draftApproveStatusRepository = draftApproveStatusRepository;
    }

    // TODO :: 승인 반려 구현
    @Transactional
    public void putApprovalDecide(long userId, long draftId, String status){

        /*int updated = draftApproveStatusRepository.updateStatus(userId, draftId, status);
        if(updated == 0) {
            throw new IllegalStateException("결재 대상이 아님");
        }
*/
    }

}
