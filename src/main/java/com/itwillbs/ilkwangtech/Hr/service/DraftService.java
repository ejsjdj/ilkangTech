package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DraftService {

    private final DraftRepository draftRepository;

    public DraftService(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public List<DraftDTO> getDraftById(Long id) {
        List<DraftEntity> drafts = draftRepository.findByMember_Id(id);

        return drafts.stream()
                .map(draft -> DraftDTO.builder()
                        .draft_id(draft.getDraft_id())
                        .draft_title(draft.getDraft_title())
                        .draft_startTime(draft.getDraft_startDate())
                        .draft_endDate(draft.getDraft_endDate())
                        .draft_approvalDate(draft.getDraft_approvalDate())
                        .draft_status(draft.getDraft_status())
                        .build())
                .collect(Collectors.toList());
    }
}
