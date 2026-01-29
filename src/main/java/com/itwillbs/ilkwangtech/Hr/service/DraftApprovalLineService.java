package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftApprovalLineDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftApprovalLineEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftApprovalLineRepository;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 결재 양식별로 결재라인 가져오기
@Service
public class DraftApprovalLineService {

    private final DraftApprovalLineRepository draftApprovalLineRepository;
    private final PositionRepository positionRepository;

    public DraftApprovalLineService(DraftApprovalLineRepository draftApprovalLineRepository, PositionRepository positionRepository){
        this.draftApprovalLineRepository = draftApprovalLineRepository;
        this.positionRepository = positionRepository;
    }

    @Transactional
    public List<DraftApprovalLineDTO> getLineByType(String type) {

        // 결재라인 조회
        List<DraftApprovalLineEntity> approvalLine = draftApprovalLineRepository.findByDraftType(type);

        // 직급명 조회
        Map<Integer, String> positionMap = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getPositionName));

        return approvalLine.stream()
                .map(line -> {
                    int positionId = line.getMember().getPosition();
                    String positionName = positionMap.getOrDefault(positionId, "Error : 직급을 조회할 수 없음");

                    return DraftApprovalLineDTO.builder()
                            .draft_type(line.getDraftType())
                            .sequence(line.getSequence())
                            .id(line.getMember().getId())
                            .name(line.getMember().getName())
                            .position(positionName)
                            .build();
                })
                .collect(Collectors.toList());
    }
}