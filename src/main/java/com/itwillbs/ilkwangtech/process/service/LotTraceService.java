package com.itwillbs.ilkwangtech.process.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.process.dto.LotDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.LotResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessStatusResponseDTO;
import com.itwillbs.ilkwangtech.process.entity.LotMaster;
import com.itwillbs.ilkwangtech.process.repository.LotMasterRepository;
import com.itwillbs.ilkwangtech.process.repository.PartProductionRepository;
import com.itwillbs.ilkwangtech.process.repository.ProductionInstructRepository;
import com.itwillbs.ilkwangtech.process.repository.QualityCheckRepository;
import com.itwillbs.ilkwangtech.process.repository.RawMaterialRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class LotTraceService {
    private final LotMasterRepository lotMasterRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final PartProductionRepository partProductionRepository;
    private final QualityCheckRepository qualityCheckRepository;
    private final ProductionInstructRepository productionInstructRepository;

    // 1. 좌측 리스트용 전체 LOT 조회
    public List<LotMaster> getAllLots() {
        return lotMasterRepository.findAll();
    }

    public List<LotResponseDTO> getAllLotsWithItemName() {
        List<LotMasterRepository.LotSummaryMapping> results = lotMasterRepository.findAllWithItemName();
        return results.stream().map(res -> {
            LotResponseDTO dto = new LotResponseDTO();
            dto.setLotId(res.getLotId());
            dto.setItemName(res.getItemName() != null ? res.getItemName() : "N/A");
            dto.setStatus(res.getStatus());
            dto.setCreatedDate(res.getCreatedDate());
            dto.setLotType(res.getLotType()); 
            return dto;
        }).collect(Collectors.toList());
    }
    
    public LotDetailResponseDTO getLotDetail(String lotId) {
        LotMasterRepository.LotDetailMapping res = lotMasterRepository.findLotDetailByLotId(lotId);
        if(res == null) return null;

        LotDetailResponseDTO dto = new LotDetailResponseDTO();
        dto.setLotId(res.getLotId());
        dto.setItemName(res.getItemName());
        dto.setInstructCode(res.getInstructCode() != null ? res.getInstructCode() : "-");
        dto.setInstructQty(res.getInstructQty());
        dto.setStatus(res.getStatus());
        dto.setLotType(res.getLotType());
        dto.setStartDate(res.getStartDate());
        dto.setEndDate(res.getEndDate());
        dto.setDefective(res.getDefective());
        return dto;
    }

    public List<ProcessStatusResponseDTO> getAllProcessStatusList() {
        List<ProductionInstructRepository.ProcessStatusMapping> results = productionInstructRepository.findAllProcessStatus();
        
        return results.stream().map(res -> {
            ProcessStatusResponseDTO dto = new ProcessStatusResponseDTO();
            dto.setInstructCode(res.getInstructCode());
            dto.setItemName(res.getItemName() != null ? res.getItemName() : "Unknown Item");
            dto.setInstructQty(res.getInstructQty());
            dto.setDefective(res.getDefective());
            dto.setStatus(res.getStatus());
            dto.setOperationName(res.getOperationName() != null ? res.getOperationName() : "대기 중"); // 공정명 매핑 
            dto.setStartDate(res.getStartDate());
            dto.setEndDate(res.getEndDate());
            return dto;
        }).collect(Collectors.toList());
    }

 // LotTraceService.java 에 추가
    public ProcessDetailResponseDTO getProcessDetailData(String instructCode) {
        ProductionInstructRepository.HeaderMapping header = productionInstructRepository.findHeaderByCode(instructCode);
        if (header == null) return null;

        // 1. 전체 불량 수량 파악 (p.defective)
        int totalDefective = header.getDefectiveQty() != null ? header.getDefectiveQty() : 0;

        ProcessDetailResponseDTO dto = new ProcessDetailResponseDTO();
        dto.setInstructCode(header.getInstructCode());
        dto.setItemName(header.getItemName());
        dto.setInstructQty(header.getInstructQty());

        // 2. 공정 상세 데이터 조회
        List<ProductionInstructRepository.DetailMapping> stepMappings = productionInstructRepository.findStepsByInstructCode(instructCode);
        
        List<ProcessDetailResponseDTO.StepDetail> steps = stepMappings.stream().map(res -> {
            ProcessDetailResponseDTO.StepDetail step = new ProcessDetailResponseDTO.StepDetail();
            step.setProcessName(res.getProcessName());
            step.setStatus(res.getStatus());
            step.setStartDate(res.getStartDate());
            step.setEndDate(res.getEndDate());
            step.setDefectiveQty(0); 
            return step;
        }).collect(Collectors.toList());

        if (totalDefective > 0 && !steps.isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(steps.size()); 
            steps.get(randomIndex).setDefectiveQty(totalDefective);
        }

        dto.setSteps(steps);
        return dto;
    }
}
