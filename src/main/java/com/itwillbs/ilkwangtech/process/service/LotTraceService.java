package com.itwillbs.ilkwangtech.process.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.process.dto.DashboardSummaryDTO;
import com.itwillbs.ilkwangtech.process.dto.LotDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.LotResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessStatusResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessStepDetailDTO;
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
    private final ProductionInstructRepository productionInstructRepository;

    // 1. 좌측 리스트용 전체 LOT 조회
    public List<LotMaster> getAllLots() {
        return lotMasterRepository.findAll();
    }

    public List<LotResponseDTO> getAllLotsWithItemName() {
        // 1. PART_PRODUCTION 및 ASSEMBLY에서 완제품/반제품 정보 가져오기 (최신순 정렬됨)
        List<LotMasterRepository.LotSummaryMapping> prodResults = lotMasterRepository.findAllProdAndSemiLots();
        
        // 2. INVENTORY에서 원자재 정보 가져오기 (M)
        List<LotMasterRepository.LotSummaryMapping> inventoryResults = lotMasterRepository.findAllInventoryAsLots();

        // 3. DTO 변환 및 합치기
        List<LotResponseDTO> combinedList = prodResults.stream().map(res -> {
            LotResponseDTO dto = new LotResponseDTO();
            dto.setLotId(res.getLotId());
            dto.setItemName(res.getItemName() != null ? res.getItemName() : "N/A");
            dto.setStatus(res.getStatus());
            dto.setCreatedDate(res.getCreatedDate());
            dto.setLotType(res.getLotType());
            return dto;
        }).collect(Collectors.toList());

        // 원자재 데이터 추가
        inventoryResults.forEach(res -> {
            LotResponseDTO dto = new LotResponseDTO();
            dto.setLotId(res.getLotId());
            dto.setItemName(res.getItemName() != null ? res.getItemName() : "N/A");
            dto.setStatus(res.getStatus());
            dto.setCreatedDate(res.getCreatedDate());
            dto.setLotType("M");
            combinedList.add(dto);
        });

        return combinedList;
    }
    
    /* LotTraceService.java */

    public LotDetailResponseDTO getLotDetail(String lotId) {
        // 1. 먼저 기본 LOT 정보를 가져와서 타입을 확인합니다.
        LotMaster master = lotMasterRepository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("LOT를 찾을 수 없습니다."));

        LotMasterRepository.LotDetailMapping res;

        // 2. 타입에 따라 다른 레포지토리 메소드 호출
        if ("F".equals(master.getLotType())) {
            res = lotMasterRepository.findFinishedLotDetail(lotId);
        } else {
            // 반제품(S, ST, IN 등)일 경우
            res = lotMasterRepository.findSemiLotDetail(lotId);
        }

        // 데이터가 없는 경우를 대비한 예외 처리
        if (res == null) return new LotDetailResponseDTO(); 

        // 3. DTO 매핑 (이 부분은 동일)
        LotDetailResponseDTO dto = new LotDetailResponseDTO();
        dto.setLotId(res.getLotId());
        dto.setItemCode(res.getItemCode());
        dto.setItemName(res.getItemName());
        dto.setInstructCode(res.getInstructCode());
        dto.setInstructQty(res.getInstructQty());
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
            Random rand = new Random(instructCode.hashCode()); 
            int randomIndex = rand.nextInt(steps.size()); 
            steps.get(randomIndex).setDefectiveQty(totalDefective);
        }

        dto.setSteps(steps);
        return dto;
    }
    
    public List<Map<String, Object>> getFinishedUsageList(String rawLotId) {
        List<LotMasterRepository.FinishedUsageMapping> results = lotMasterRepository.findFinishedLotsByRawLotId(rawLotId);
        log.info(">>>> 역추적 결과 개수: " + results.size()); // 로그 추가로 데이터 유무 확인
        
        return results.stream().map(res -> {
            Map<String, Object> map = new HashMap<>();
            map.put("lotId", res.getLotId());
            map.put("itemName", res.getItemName());
            map.put("quantity", res.getQuantity());
            map.put("status", res.getStatus());
            return map;
        }).collect(Collectors.toList());
    }
    
    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        dto.setTodayInstructCount(0); 
        dto.setInProgressCount(3);
        dto.setQcFailedCount(0);
        return dto;
    }
    
    public ProcessStepDetailDTO getProcessStepDetail(String instructCode) {
        LotMasterRepository.ProcessStepDetailMapping res = lotMasterRepository.findStepDetailByInstructCode(instructCode);
        
        if (res == null) return null;

        ProcessStepDetailDTO dto = new ProcessStepDetailDTO();
        dto.setOperationName(res.getOperationName());
        dto.setOperationId(res.getOperationId());
        dto.setMemberId(res.getMemberId());
        dto.setMemberName(res.getMemberName());
        dto.setOperationQty(res.getOperationQty());
        dto.setDefectiveQty(res.getDefectiveQty());
        dto.setEquipName(res.getEquipName());
        dto.setEquipCode(res.getEquipCode());
        
        return dto;
    }
}
