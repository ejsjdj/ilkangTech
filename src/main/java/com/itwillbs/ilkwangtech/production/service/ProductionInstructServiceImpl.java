package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
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
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        productionInsturctRepository.save(header);
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
        // 1. 현재 작업자 상태 변경
        ProductionWorkerEntity workerEntity = productionWorkerRepository
                .findById(workerId).orElseThrow(() -> new IllegalArgumentException("작업자 정보가 없습니다."));
        workerEntity.setStatus("COMPLETE");

        // 2. 상위 작업지시(Instruct) 엔티티 가져오기
        ProductionInstructEntity instructEntity = productionInsturctRepository
                .findById(instructId).orElseThrow(() -> new IllegalArgumentException("작업지시 정보가 없습니다."));

        // 3. 모든 작업자가 COMPLETE인지 확인
        boolean allWorkersComplete = instructEntity.getWorkers().stream()
                .allMatch(w -> "COMPLETE".equals(w.getStatus()));

        if (allWorkersComplete) {
            // 4. 작업지시 상태를 COMPLETE로 변경
            instructEntity.setStatus("COMPLETE");

            // 5. 상위 생산계획(Plane) 엔티티 가져오기
            ProductionPlaneEntity planeEntity = productionPlaneRepository
                    .findById(planeId).orElseThrow(() -> new IllegalArgumentException("생산계획 정보가 없습니다."));

            // 6. 해당 생산계획에 속한 모든 작업지시(Instruct)가 COMPLETE인지 확인
            boolean allInstructsComplete = planeEntity.getInstruct().stream()
                    .allMatch(i -> "COMPLETE".equals(i.getStatus()));

            if (allInstructsComplete) {
                // 7. 생산계획 상태를 COMPLETE로 변경
                planeEntity.setStatus("COMPLETE");
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

    // 4. 불량 등록
//    public void completeInstructDefective(Long defectiveQty, String instructCode, Long processId){
//
//    }
}