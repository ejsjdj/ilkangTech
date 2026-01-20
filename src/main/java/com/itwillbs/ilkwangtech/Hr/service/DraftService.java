package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.Hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

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
        List<DraftDTO> written = draftRepository.findByMember_Id(id);
        List<DraftDTO> approve = draftRepository.findApproveDrafts(id);
        return Stream.concat(written.stream(), approve.stream()).
                distinct().
                toList();
    }
}
