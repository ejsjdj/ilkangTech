package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductionPlaneService {

    // 1. 생산계획 목록 조회
    Page<ProductionPlaneDTO> getProductionPlaneList(Pageable pageable, String keyword);

    // 2. 생산계획 상세 조회
    Optional<ProductionPlaneDetailDTO> getProductionPlaneDetail(Long productionId);

    // 3. 신규 생산계획 등록
    void saveProductionPlane(ProductionPlaneInsertDTO productionPlaneInsertDTO, Long userId);
}
