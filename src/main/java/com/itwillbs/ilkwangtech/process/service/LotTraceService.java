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
import com.itwillbs.ilkwangtech.process.repository.ProductionInstructRepository;

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
    	List<LotMasterRepository.LotSummaryMapping> prodResults = lotMasterRepository.findAllProdAndSemiLotsFromWorker();
        
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
            dto.setLotType("R");
            combinedList.add(dto);
        });

        return combinedList;
    }
    
    public Object getLotDetail(String lotId) {
        // 원자재인 경우
        if (lotId.startsWith("RW-")) {
            return getRawMaterialDetail(lotId);
        }
        
        // 완제품/반제품인 경우 새로운 쿼리 실행
        LotMasterRepository.ProdLotDetailMapping res = lotMasterRepository.findProdLotDetail(lotId);
        if (res == null) return null;
        
        Map<String, Object> map = new HashMap<>();
        map.put("lotId", res.getLotId());
        map.put("itemCode", res.getItemCode());
        map.put("itemName", res.getItemName());
        map.put("instructCode", res.getInstructCode());
        map.put("productionQty", res.getProductionQty());
        map.put("defectiveQty", res.getDefectiveQty());
        map.put("endTime", res.getEndTime());
        map.put("isRawMaterial", false); 
        return map;
    }
    
    public LotMasterRepository.ProdSubDetailMapping getProdSubDetailInfo(String lotId) {
        return lotMasterRepository.findProdSubDetailByLotId(lotId);
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
    
    public Map<String, Object> getRawMaterialDetail(String lotId) {
        LotMasterRepository.RawMaterialDetailMapping res = lotMasterRepository.findRawMaterialDetail(lotId);
        if (res == null) return null;
        
        Map<String, Object> map = new HashMap<>();
        map.put("lotId", res.getLotId());
        map.put("itemCode", res.getItemCode());
        map.put("itemName", res.getItemName());
        map.put("uom", res.getUom());
        map.put("itemType", res.getItemType());
        map.put("currentQuantity", res.getCurrentQuantity());
        
        String expDateStr = "-";
        if (res.getExpirationDate() != null) {
            expDateStr = res.getExpirationDate().toLocalDate().toString(); 
        }
        map.put("expirationDate", expDateStr);
        
        map.put("zone", res.getZone());
        map.put("isRawMaterial", true); // 프론트엔드 구분용
        return map;
    }

    public LotMasterRepository.SubDetailMapping getSubDetailInfo(String lotId) {
        return lotMasterRepository.findSubDetailByLotId(lotId);
    }
}
