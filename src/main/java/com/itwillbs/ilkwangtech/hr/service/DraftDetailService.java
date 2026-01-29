package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.DraftDetailDTO;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
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
                orElseThrow(() -> new IllegalArgumentException("문서가 삭제되었거나 존재하지 않습니다"));

        return DraftDetailDTO.builder()
                .detailTitle(draftDetail.getDraftTitle())
                .detailContent(draftDetail.getDraftContent())
                .detailFile(draftDetail.getDraftFile())
                .detailStartDate(draftDetail.getDraftStartDate())
                .detailEndDate(draftDetail.getDraftEndDate())
                .build();
    }
}
