package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DraftService {

    private final DraftRepository draftRepository;
    private final DraftApproveStatusRepository draftApproveStatusRepository;

    public DraftService(DraftRepository draftRepository, DraftApproveStatusRepository draftApproveStatusRepository) {
        this.draftRepository = draftRepository;
        this.draftApproveStatusRepository = draftApproveStatusRepository;
    }

    // 내가 작성한 문서 + 내가 결재자인 문서 조회
    @Transactional
    public List<DraftDTO> getDraftById(Long id) {
        return draftRepository.findAllMyDrafts(id);
    }
}
