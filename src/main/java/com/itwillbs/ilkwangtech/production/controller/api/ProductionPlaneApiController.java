package com.itwillbs.ilkwangtech.production.controller.api;

import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/production/plane")
@Log4j2
public class ProductionPlaneApiController {

    /**
     * 생산계획 목록 조회 API
     * 검색조건 : 제품명, 계획번호
     * 페이징 조건 포함
     */

    public Page<ProductionPlaneDTO> getProductionPlane(){
        return null;
    }

    // 2. 생산계획 상세

    public void getProductionPlaneDetail(){

    }

    // 3. 생산계획 등록

    public void insertProductionPlane(){

    }


    // 4. 생산계획 취소

    public void cancelProductionPlane(){

    }

}