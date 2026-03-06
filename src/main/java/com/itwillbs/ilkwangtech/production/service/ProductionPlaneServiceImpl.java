package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneInsertDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneItemDTO;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneDetailEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// 생산계획 서비스 구현체
@Service
@RequiredArgsConstructor
public class ProductionPlaneServiceImpl implements ProductionPlaneService {

    private final ProductionPlaneRepository productionPlaneRepository;
    private final MemberRepository memberRepository;
    private final ProcessRouteRepository processRouteRepository;

    // 1. 생산계획 목록 조회
    @Override
    @Transactional
    public Page<ProductionPlaneDTO> getProductionPlaneList(Pageable pageable, String keyword){

        Page<ProductionPlaneEntity> productionPlaneEntities = productionPlaneRepository.findByKeyword(pageable, keyword);

        return productionPlaneEntities
                .map(ProductionPlaneDTO::fromList);
    }

    // 2. 생산계획 상세 조회
    @Override
    @Transactional
    public Optional<ProductionPlaneDetailDTO> getProductionPlaneDetail(Long productionId){

        Optional<ProductionPlaneEntity> productionPlaneEntity = productionPlaneRepository.findDetailById(productionId);

        return productionPlaneEntity.map(ProductionPlaneDetailDTO::fromDetail);
    }

    // 3. 신규 생산계획 등록
    @Override
    @Transactional
    public void saveProductionPlane(ProductionPlaneInsertDTO productionPlaneInsertDTO, Long userId){
        // 1. 유저 정보 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

        ProcessRouteEntity routeCode = processRouteRepository.findById(productionPlaneInsertDTO.getRouteCode()).
                orElseThrow(() -> new IllegalArgumentException("라우트 코드가 존재하지 않습니다."));


        // 2. 헤더 엔티티 저장
        ProductionPlaneEntity header = ProductionPlaneEntity.saveHeader(
                routeCode,
                productionPlaneInsertDTO.getPlaneCode(),
                productionPlaneInsertDTO.getPlaneDate(),
                member,
                productionPlaneInsertDTO.getItem(),
                productionPlaneInsertDTO.getTotalQty(),
                productionPlaneInsertDTO.getStatus(),
                productionPlaneInsertDTO.getMemo()
                );


        // 3. 상세 엔티티 저장
        for(ProductionPlaneItemDTO itemDTO : productionPlaneInsertDTO.getDetails()){

            ProductionPlaneDetailEntity detail = ProductionPlaneDetailEntity.create(
                    itemDTO.getOrderId(),
                    itemDTO.getProductQty(),
                    itemDTO.getMemo()
            );

            header.saveDetails(detail);

        }
    }

    @Override
    @Transactional
    // 4. 생산계획 및 작업지시 취소
    public void cancelProductionPlane(Long planeId){
        productionPlaneRepository.updatePlaneStatus(planeId);
        productionPlaneRepository.updateInstructStatusByPlaneId(planeId);
    }

}
