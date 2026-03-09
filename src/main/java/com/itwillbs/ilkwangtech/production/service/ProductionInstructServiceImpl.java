package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryHistoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryHistoryRepository;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.production.dto.*;
import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionInsturctRepository;
import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
import com.itwillbs.ilkwangtech.production.repository.ProductionWorkerRepository;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderLineDTO;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.standard.entity.BomEntity;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.repository.BomRepository;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductionInstructServiceImpl implements ProductionInstructService {

    private final ProductionInsturctRepository productionInsturctRepository;
    private final MemberRepository memberRepository;
    private final ProcessRepository processRepository;
    private final ProductionPlaneRepository productionPlaneRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    
    private final BomRepository bomRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final ProductionWorkerRepository productionWorkerRepository;


    @Override
    @Transactional
    // 1. 작업지시 목록
    public Page<ProductionInstructDTO> getProductionInstructList(Pageable pageable, String keyword){

        Page<ProductionInstructEntity> productionInstructEntities = productionInsturctRepository.findByKeyword(pageable, keyword);

        return productionInstructEntities
                .map(ProductionInstructDTO::fromList);
    }

    @Override
    @Transactional
    // 2. 작업지시 상세
    public Optional<ProductionInstructDetailDTO> getProductionInstructDetail(Long instructId){

        Optional<ProductionInstructEntity> productionInstructEntity = productionInsturctRepository.findById(instructId);

        return productionInstructEntity.map(ProductionInstructDetailDTO::fromList);
    }

    @Override
    @Transactional
    // 3. 작업지시 등록
    public void saveProductionInstruct(ProductionInstructInsertDTO productionInstructInsertDTO, Long userId){

        // 1. 공정정보 확인
        ItemEntity item = itemRepository.findById(productionInstructInsertDTO.getItem()).
                orElseThrow(() -> new IllegalArgumentException("공정 코드가 존재하지 않습니다."));

        // 2. 생산계획 확인
        ProductionPlaneEntity production = productionPlaneRepository.findById(productionInstructInsertDTO.getProductionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 생산계획이 존재하지 않습니다."));

        ProductionInstructEntity header = ProductionInstructEntity.saveHeader(
                productionInstructInsertDTO.getInstructCode(),
                production,
                item,
                productionInstructInsertDTO.getInstructQty(),
                LocalDateTime.now(),
                productionInstructInsertDTO.getStatus(),
                productionInstructInsertDTO.getDefective()
        );

        // 2. 작업자 엔티티 저장
        for(ProductionInstructWorkerInsertDTO lineDTO : productionInstructInsertDTO.getWorkers()){

            System.out.println("processId = " + lineDTO.getProcessId());
            System.out.println("memberId = " + lineDTO.getMemberId());
            System.out.println("outputItemId = " + lineDTO.getOutputItemId());
            System.out.println("additionQty = " + lineDTO.getAdditionQty());

            Member memberId = memberRepository.findById(lineDTO.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다."));

            ProcessEntity processId = processRepository.findById(lineDTO.getProcessId()).
                    orElseThrow(() -> new IllegalArgumentException("공정이 존재하지 않습니다."));

            ItemEntity outputItem = itemRepository.findById(lineDTO.getOutputItemId())
                    .orElseThrow(() -> new IllegalArgumentException("품목정보가 없습니다."));

            System.out.println("작업자 엔티티 item_id : " + lineDTO.getOutputItemId());

            ProductionWorkerEntity line = ProductionWorkerEntity.create(
                    processId,
                    memberId,
                    "LOT-TEST-01",
                    lineDTO.getStartTime(),
                    lineDTO.getEndTime(),
                    lineDTO.getProductionQty(),
                    lineDTO.getAdditionQty(),
                    outputItem,
                    lineDTO.getSequence()
            );

            header.saveLine(line);
        }
        
        // 1. 작업지시 DB 저장
        productionInsturctRepository.save(header);

        List<BomEntity> mainBomList = bomRepository.findByChildItem_ItemId(item.getItemId());
        deductMaterials(mainBomList, productionInstructInsertDTO.getInstructQty());

        for(ProductionInstructWorkerInsertDTO lineDTO : productionInstructInsertDTO.getWorkers()){
            if (lineDTO.getAdditionQty() != null && lineDTO.getAdditionQty() > 0) {
                List<BomEntity> subBomList = bomRepository.findByChildItem_ItemId(lineDTO.getOutputItemId());
                deductMaterials(subBomList, lineDTO.getAdditionQty());
            }
        }
    }
    
    // 재고 차감 공통 로직
    private void deductMaterials(List<BomEntity> bomList, Long qtyMultiplier) {
        if (bomList == null || bomList.isEmpty()) return;

        for (BomEntity bom : bomList) {
            ItemEntity material = bom.getParentItem();
            long requiredQty = bom.getRequireQty() * qtyMultiplier; // 필요수량 * (메인수량 or 추가수량)

            List<InventoryEntity> stocks = inventoryRepository.findByItemItemIdOrderByExpirationDateAsc(material.getItemId());
            long remainingToDeduct = requiredQty;

            for (InventoryEntity stock : stocks) {
                if (remainingToDeduct <= 0) break;

                long currentQty = stock.getCurrentQuantity();
                if (currentQty == 0) continue;

                long deductQty = Math.min(currentQty, remainingToDeduct);
                stock.setCurrentQuantity(currentQty - deductQty); 
                remainingToDeduct -= deductQty;

                InventoryHistoryEntity history = new InventoryHistoryEntity();
                history.setItem(material);
                history.setTransactionDate(LocalDate.now());
                history.setTransactionType("OUT");
                history.setQuantity(deductQty);
                inventoryHistoryRepository.save(history);
            }
        }
    }


    // 5. 작업시작(공정 시작)
    @Override
    @Transactional
    public void startInstruct(Long planeId, Long instructId, Long workerId){
        ProductionWorkerEntity workerEntity = productionWorkerRepository
                .findById(workerId).orElseThrow(() -> new IllegalArgumentException("작업지시 공정정보가 없습니다."));

        ProductionInstructEntity instructEntity = productionInsturctRepository.findById(instructId)
                .orElseThrow(() -> new IllegalArgumentException("작업지시 정보가 없습니다."));

        ProductionPlaneEntity planeEntity  = productionPlaneRepository.findById(planeId)
                .orElseThrow(() -> new IllegalArgumentException("생산계획 정보가 없습니다."));

        workerEntity.setStatus("PROGRESS");
        instructEntity.setStatus("PROGRESS");
        planeEntity.setStatus("PROGRESS");


    }

    // 작업지시 공정 완료
    @Override
    @Transactional
    public void completeInstruct(Long planeId, Long instructId, Long workerId){

        // 1. 작업자 상태 변경
        ProductionWorkerEntity workerEntity = productionWorkerRepository.findById(workerId).orElseThrow();
        workerEntity.setStatus("COMPLETE");

        // 2. 작업지시 엔티티 가져오기
        ProductionInstructEntity currentInstruct = productionInsturctRepository.findById(instructId).orElseThrow();

        // 3. 모든 작업자가 COMPLETE인지 확인
        boolean allWorkersComplete = currentInstruct.getWorkers().stream()
                .allMatch(w -> "COMPLETE".equals(w.getStatus()));

        if (allWorkersComplete) {
            currentInstruct.setStatus("COMPLETE");

            productionInsturctRepository.saveAndFlush(currentInstruct);

            // 5. 생산계획 엔티티 가져오기
            ProductionPlaneEntity planeEntity = productionPlaneRepository.findById(planeId).orElseThrow();

            // 6. 상태 검사
            long totalInstructs = planeEntity.getInstruct().size();
            long completeCount = planeEntity.getInstruct().stream()
                    .filter(i -> "COMPLETE".equals(i.getStatus()))
                    .count();

            if (totalInstructs == completeCount) {
                planeEntity.setStatus("COMPLETE");
                productionPlaneRepository.save(planeEntity);
            }
        }
    }

    // 작업지시 공정 중단
    @Override
    @Transactional
    public void cancelInstruct(Long planeId, Long instructId, Long workerId){
        ProductionWorkerEntity workerEntity = productionWorkerRepository
                .findById(workerId).orElseThrow(() -> new IllegalArgumentException("작업지시 공정정보가 없습니다."));

        ProductionInstructEntity instructEntity = productionInsturctRepository.findById(instructId)
                .orElseThrow(() -> new IllegalArgumentException("작업지시 정보가 없습니다."));

        ProductionPlaneEntity planeEntity  = productionPlaneRepository.findById(planeId)
                .orElseThrow(() -> new IllegalArgumentException("생산계획 정보가 없습니다."));

        workerEntity.setStatus("CACEL");
        instructEntity.setStatus("CACEL");
        planeEntity.setStatus("CACEL");
    }
}