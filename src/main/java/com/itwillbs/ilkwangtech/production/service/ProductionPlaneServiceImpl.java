package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDetailDTO;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 생산계획 서비스 구현체
@Service
@RequiredArgsConstructor
public class ProductionPlaneServiceImpl implements ProductionPlaneService {

    private final ProductionPlaneRepository productionPlaneRepository;

    // 1. 생산계획 목록 조회
    public Page<ProductionPlaneDTO> getProductionPlaneList(Pageable pageable, String keyword){

        Page<ProductionPlaneEntity> productionPlaneEntities = productionPlaneRepository.findByKeyword(pageable, keyword);

        return productionPlaneEntities
                .map(ProductionPlaneDTO::fromList);
    }

    // 2. 생산계획 상세 조회
    public Optional<ProductionPlaneDetailDTO> getProductionPlaneDetail(Long productionId){

        Optional<ProductionPlaneEntity> productionPlaneEntity = productionPlaneRepository.findDetailById(productionId);

        return productionPlaneEntity.map(ProductionPlaneDetailDTO::fromDetail);
    }

}
