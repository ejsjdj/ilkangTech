package com.itwillbs.ilkwangtech.production.controller.api;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneInsertDTO;
import com.itwillbs.ilkwangtech.production.service.ProductionPlaneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/api/production/plane")
@RequiredArgsConstructor
@Log4j2
public class ProductionPlaneApiController {


    private final ProductionPlaneService productionPlaneService;

    /**
     * 생산계획 목록 조회 API
     * 검색조건 : 제품명, 계획번호
     * 페이징 조건 포함
     */
    public Page<ProductionPlaneDTO> getProductionPlane(Pageable pageable,
                                                       @RequestParam("keyword") String keyword){

        log.info("생산계획 목록 조회 조건 - 페이지네이션: {}, 키워드: {}", pageable, keyword);

        Page<ProductionPlaneDTO> list = productionPlaneService.getProductionPlaneList(pageable, keyword);

        log.info("생산계획 목록 조회 결과 - DTO: {}", list);

        return list;
    }

    // 2. 생산계획 상세
    public Optional<ProductionPlaneDetailDTO> getProductionPlaneDetail(@RequestParam("productionId") Long productionId){

        Optional<ProductionPlaneDetailDTO> detail = productionPlaneService.getProductionPlaneDetail(productionId);

        return detail;
    }

    // 3. 생산계획 등록
    public void insertProductionPlane(@RequestBody ProductionPlaneInsertDTO productionPlaneInsertDTO,
                                      @AuthenticationPrincipal AccountLogin accountLogin){

        Long userId = accountLogin.getId();

        productionPlaneService.saveProductionPlane(productionPlaneInsertDTO, userId);
    }


    // 4. 생산계획 취소
    public void cancelProductionPlane(){

    }

}