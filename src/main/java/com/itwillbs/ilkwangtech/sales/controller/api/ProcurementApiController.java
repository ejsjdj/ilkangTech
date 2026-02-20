package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Log4j2
public class ProcurementApiController {

    // 1. 발주 리스트 조회
    @GetMapping("/procurement")
    public Page<PurchaseOrderDTO> getProcurement(Pageable pageable,
                                                 @RequestParam("startDate") String startDate,
                                                 @RequestParam("endDate") String endDate,
                                                 @RequestParam("searchType") String searchType,
                                                 @RequestParam("keyWord") String keyWord
                                               ){

        log.info("발주 리스트 조회 - 발주 시작일: {}, 발주 종료일: {}, 검색필터: {}, 검색어: {}",startDate, endDate, searchType, keyWord);

        // log.info("발주 리스트 조회 결과 - 리스트: {}", );

        return null;
    }

    // 2. 발주 상세 내용

    // 3. 발주 등록

}