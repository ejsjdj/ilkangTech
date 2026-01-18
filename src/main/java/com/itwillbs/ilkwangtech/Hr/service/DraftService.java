package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DraftService {

    private final DraftRepository draftRepository;

    public DraftService(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    @Transactional
    public List<DraftDTO> getDraftById(Long id) {
        List<DraftEntity> drafts = draftRepository.findByMember_Id(id);

        return drafts.stream()
                .map(draft -> DraftDTO.builder()
                        .draft_id(draft.getDraft_id())
                        .draft_title(draft.getDraftTitle())
                        .draft_startTime(draft.getDraftStartDate())
                        .draft_endDate(draft.getDraftEndDate())
                        .draft_status(draft.getDraftStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
