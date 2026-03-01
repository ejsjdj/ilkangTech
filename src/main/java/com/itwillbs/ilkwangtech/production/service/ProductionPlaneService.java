package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.production.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductionPlaneService {

    // 1. 생산계획 목록 조회
    Page<ProductionPlaneDTO> getProductionPlaneList(Pageable pageable, String keyword);

    // 2. 생산계획 상세 조회
    Optional<ProductionPlaneDetailDTO> getProductionPlaneDetail(Long productionId);

    // 3. 신규 생산계획 등록
    void saveProductionPlane(ProductionPlaneInsertDTO productionPlaneInsertDTO, Long userId);

    // 4. 생산계획 및 작업지시 취소
    void cancelProductionPlane(Long instructId);

    // 5. 작업지시에서 생산계획 불러오기
    List<ProcessRegisterDTO> getProcessInstructList(Long planeId);

    // 6. 생산계획 전체 조회
    List<ProductionPlaneAllDTO> getProductionPlaneAll();
}
