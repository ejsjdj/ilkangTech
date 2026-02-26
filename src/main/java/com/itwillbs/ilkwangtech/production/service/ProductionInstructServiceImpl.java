package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructInsertDTO;
import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionInsturctRepository;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRepository;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRouteRepository;
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
    private final ProcessRouteRepository processRouteRepository;
    private final ProcessRepository processRepository;

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

        // 1. 유저 정보 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

        ProcessRouteEntity routeCode = processRouteRepository.findById(productionInstructInsertDTO.getRouteCode()).
                orElseThrow(() -> new IllegalArgumentException("라우트 코드가 존재하지 않습니다."));

//        ProcessEntity process = processRepository.findById(productionInstructInsertDTO.getProcess())
//                .orElseThrow(() -> new IllegalArgumentException("공정 코드가 존재하지 않습니다."));

        ProductionInstructEntity header = ProductionInstructEntity.saveHeader(
                productionInstructInsertDTO.getInstructCode(),
                productionInstructInsertDTO.getLotId(),
//                productionInstructInsertDTO.getProductionId(),
//                productionInstructInsertDTO.getDetailId(),
                productionInstructInsertDTO.getItem(),
                // productionInstructInsertDTO.getProcess(),
                productionInstructInsertDTO.getWorkers(),
                productionInstructInsertDTO.getInstructQty(),
                productionInstructInsertDTO.getStartDate(),
                productionInstructInsertDTO.getEndDate(),
                productionInstructInsertDTO.getStatus(),
                productionInstructInsertDTO.getDefective()
        );

    }

}