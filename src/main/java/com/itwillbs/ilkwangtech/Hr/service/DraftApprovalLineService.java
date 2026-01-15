package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftApprovalLineDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftApprovalLineEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftApprovalLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 결재 양식별로 결재라인 가져오기
@Service
public class DraftApprovalLineService {

    private final DraftApprovalLineRepository draftApprovalLineRepository;

    public DraftApprovalLineService(DraftApprovalLineRepository draftApprovalLineRepository){
        this.draftApprovalLineRepository = draftApprovalLineRepository;
    }

    public List<DraftApprovalLineDTO> getLineByType(String type) {

        List<DraftApprovalLineEntity> approvalLine = draftApprovalLineRepository.findByDraftType(type);

        return approvalLine.stream()
                .map(line -> DraftApprovalLineDTO.builder()
                        .draft_type(line.getDraftType())
                        .sequence(line.getSequence())
                        .name(line.getMember().getName())
                        .position(line.getMember().getPosition())
                        .build())
                .collect(Collectors.toList());

    }
}
