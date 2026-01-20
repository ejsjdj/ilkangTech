package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftDetailDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DraftDetailService {

    private final DraftRepository draftRepository;

    public DraftDetailService(DraftRepository draftRepository){
        this.draftRepository = draftRepository;
    }

    @Transactional
    public DraftDetailDTO getDraftDetail(Long userId, Long draftId){

        // 문서 상세내용 조회
        DraftEntity draftDetail = draftRepository.findById(draftId).
                orElseThrow(() -> new IllegalArgumentException("문서가 삭제되었거나 존재하지 않습니다"));;

        return DraftDetailDTO.builder()
                .title(draftDetail.getDraftTitle())
                .content(draftDetail.getDraftContent())
                .file(draftDetail.getDraftFile())
                .startDate(draftDetail.getDraftStartDate())
                .endDate(draftDetail.getDraftEndDate())
                .build();
    }
}
