package com.itwillbs.ilkwangtech.process.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.process.dto.DashboardSummaryDTO;
import com.itwillbs.ilkwangtech.process.dto.LotResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessStatusResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessStepDetailDTO;
import com.itwillbs.ilkwangtech.process.entity.LotMaster;
import com.itwillbs.ilkwangtech.process.repository.LotMasterRepository;
import com.itwillbs.ilkwangtech.process.repository.ProcessWorkerRepository;
import com.itwillbs.ilkwangtech.process.repository.ProductionInstructRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class LotTraceService {
    private final LotMasterRepository lotMasterRepository;
    private final ProductionInstructRepository productionInstructRepository;
    private final ProcessWorkerRepository processWorkerRepository;

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
    
 // LotTraceService.java 내부

    public Object getLotDetail(String lotId) {
        if (lotId.startsWith("RW-")) {
            Map<String, Object> rawMap = getRawMaterialDetail(lotId);
            // 원자재 데이터가 없을 때 뻗는 것 방지
            if (rawMap == null) return new HashMap<>(); 
            return rawMap;
        }
        
        LotMasterRepository.ProdLotDetailMapping res = lotMasterRepository.findProdLotDetail(lotId);
        
        if (res == null) {
            Map<String, Object> emptyMap = new HashMap<>();
            emptyMap.put("lotId", lotId);
            emptyMap.put("isRawMaterial", false);
            return emptyMap; 
        }
        
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
    	List<ProductionInstructRepository.HeaderMapping> headers = productionInstructRepository.findHeaderByCode(instructCode);
    	
    	if (headers == null || headers.isEmpty()) {
            log.warn(">>> [" + instructCode + "] 해당 공정의 헤더 데이터가 없습니다.");
            return new ProcessDetailResponseDTO(); 
        }
    	
    	ProductionInstructRepository.HeaderMapping header = headers.get(0);

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
    
    public List<LotMasterRepository.ProdProcessMapping> getProcessesByLotId(String lotId) {
        return lotMasterRepository.findProcessesByLotId(lotId);
    }

    public List<LotMasterRepository.FinishedUsageMapping> getMaterialsByLotId(String lotId) {
        return lotMasterRepository.findRecursiveMaterialsByLotId(lotId);
    }
    
    // 불량 발생 리스트 조회
    public List<Map<String, Object>> getDefectiveProcessList() {
        List<ProductionInstructRepository.DefectiveMapping> results = productionInstructRepository.findDefectiveProcesses();
        
        return results.stream().map(res -> {
            Map<String, Object> map = new HashMap<>();
            map.put("instructCode", res.getInstructCode());
            map.put("itemName", res.getItemName());
            map.put("processName", res.getProcessName());
            map.put("defectiveQty", res.getDefectiveQty());
            map.put("endDate", res.getEndDate());
            return map;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public void updateWorkerTime(Long workerId, String type) {
        if ("START".equals(type)) {
            productionInstructRepository.updateStartTime(workerId);
        } else if ("END".equals(type)) {
            productionInstructRepository.updateEndTime(workerId);
        }
    }
    
    @Transactional
    public void startProcessWork(Long workerId) {
        // 시작 시간 기록
    	processWorkerRepository.updateStartTime(workerId);
    }

    @Transactional
    public void completeProcessWork(Long workerId) {
        // 1. 해당 작업(공정)의 기본 정보 가져오기
    	ProcessWorkerRepository.WorkerInfoMapping info = processWorkerRepository.findWorkerInfoById(workerId);
        if (info == null) return;

        // 2. LOT TYPE 결정 (공정명 기준)
        String processName = info.getProcessName();
        String lotType = "ETC";
        if (processName.contains("프레스")) lotType = "ST";
        else if (processName.contains("사출")) lotType = "IN";
        else if (processName.contains("도장") || processName.contains("도색")) lotType = "PT";
        else if (processName.contains("조립")) lotType = "SAM";

        // 3. LOT 코드 생성
        // 3-1. 날짜 (예: 20260308)
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 3-2. 작업지시번호 생략버전 (예: INS300 추출)
        String fullInstructCode = info.getInstructCode();
        String[] codeParts = fullInstructCode.split("-");
        String shortInstructCode = codeParts[codeParts.length - 1]; // 맨 마지막 부분 추출
        
        // 3-3. 시퀀스 채번 (오늘 날짜, 동일 아이템 기준)
        Integer seq = processWorkerRepository.getNextLotSequence(info.getItemCode());
        String seqStr = String.format("%03d", seq); // 3자리 숫자 (예: 001)

        // 완성된 LOT_ID 조립 (예: ST-008-20260307-INS300-001)
        String generatedLotId = String.format("%s-%s-%s-%s", info.getItemCode(), dateStr, shortInstructCode, seqStr);

        // 4. PARENT_LOT_ID 찾기 (이전 공정의 LOT)
        String parentLotId = processWorkerRepository.findParentLotId(info.getInstructId(), workerId);

        // 5. lot_master 테이블에 Insert (회원님 정정 내역 반영: product_id에 item_code 삽입)
        processWorkerRepository.insertLotMaster(
                generatedLotId, 
                lotType, 
                parentLotId, 
                info.getItemCode(), 
                info.getProductionQty()
        );

        // 6. production_worker 테이블의 end_time 업데이트 및 생성된 lot_id 부여
        processWorkerRepository.updateEndTimeAndLotId(workerId, generatedLotId);
    }
}
