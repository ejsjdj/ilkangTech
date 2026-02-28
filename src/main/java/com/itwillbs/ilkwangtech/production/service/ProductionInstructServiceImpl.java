package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.production.dto.*;
import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionInsturctRepository;
import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderLineDTO;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductionInstructServiceImpl implements ProductionInstructService {

    private final ProductionInsturctRepository productionInsturctRepository;
    private final MemberRepository memberRepository;
    private final ProcessRepository processRepository;
    private final ProductionPlaneRepository productionPlaneRepository;

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
        ProcessEntity process = processRepository.findById(productionInstructInsertDTO.getProcessCode()).
                orElseThrow(() -> new IllegalArgumentException("공정 코드가 존재하지 않습니다."));

        // 2. 생산계획 확인
        ProductionPlaneEntity production = productionPlaneRepository.findById(productionInstructInsertDTO.getProductionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 생산계획이 존재하지 않습니다."));

        ProductionInstructEntity header = ProductionInstructEntity.saveHeader(
                productionInstructInsertDTO.getInstructCode(),
                productionInstructInsertDTO.getLotId(),
                production,
                productionInstructInsertDTO.getItem(),
                process,
                productionInstructInsertDTO.getInstructQty(),
                productionInstructInsertDTO.getStartDate(),
                productionInstructInsertDTO.getEndDate(),
                productionInstructInsertDTO.getStatus(),
                productionInstructInsertDTO.getDefective()
        );

        // 2. 작업자 엔티티 저장
        for(ProductionInstructWorkerInsertDTO lineDTO : productionInstructInsertDTO.getWorkers()){

            Member memberId = memberRepository.findById(lineDTO.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다."));

            ProcessEntity processId = processRepository.findById(lineDTO.getOperationId()).
                    orElseThrow(() -> new IllegalArgumentException("라우트 코드가 존재하지 않습니다."));

            ProductionWorkerEntity line = ProductionWorkerEntity.create(
                    processId,
                    memberId
            );


            header.saveLine(line);
        }

    }

    // 4. 불량 등록
    public void updateInstructDefective(Long defectiveQty, String instructCode, Long processId){

    }

    // 5. 작업지시 완료
    public void updateInstruct(String instructCode, Long processId){
        productionInsturctRepository.updateInstructCompleteStatus(instructCode, processId);
    }
}