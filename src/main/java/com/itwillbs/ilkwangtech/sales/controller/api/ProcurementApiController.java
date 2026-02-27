package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderInsertDTO;
import com.itwillbs.ilkwangtech.sales.service.purchaseorder.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Log4j2
public class ProcurementApiController {

    private final PurchaseOrderService purchaseOrderService;

    // 1. 발주 리스트 조회
    @GetMapping("/procurement")
    public Page<PurchaseOrderDTO> getProcurement(Pageable pageable,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                 @RequestParam(required = false) String searchType,
                                                 @RequestParam(required = false) String keyword
                                               ){

        log.info("발주 리스트 조회 - 발주 시작일: {}, 발주 종료일: {}, 검색필터: {}, 검색어: {}",startDate, endDate, searchType, keyword);

        Page<PurchaseOrderDTO> purchaseOrderDTO = purchaseOrderService.getPurchaseOrderList(pageable, startDate, endDate, searchType, keyword);

        log.info("발주 리스트 조회 - 결과: {}", purchaseOrderDTO );

        return purchaseOrderDTO;
    }

    // 2. 발주 상세 조회
    @GetMapping("/procurement_deail")
    public PurchaseOrderDetailDTO getProcurementDetail(@RequestParam("purchaseOrderId") Long purchaseOrderId){

        log.info("발주 상세 조회 - 발주 ID: {}", purchaseOrderId);

        PurchaseOrderDetailDTO detailDTO = purchaseOrderService.getPurchaseOrderDetail(purchaseOrderId);

        log.info("발주 상세 조회 - 결과: {}", detailDTO);

        return detailDTO;
    }

    // 3. 신규 발주 등록
    @PostMapping("/procurement/insert")
    public void saveProcurement(@RequestBody PurchaseOrderInsertDTO purchaseOrderInsertDTO,
                                @AuthenticationPrincipal AccountLogin accountLogin){

        Long userId = accountLogin.getId();

        purchaseOrderService.savePurchaseOrder(purchaseOrderInsertDTO, userId);
    }

}