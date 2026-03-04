package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.sales.dto.*;
import com.itwillbs.ilkwangtech.sales.service.purchaseorder.PurchaseOrderService;
import com.itwillbs.ilkwangtech.sales.service.purchasereturn.PurchaseReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Log4j2
public class ProcurementApiController {

    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseReturnService purchaseReturnService;

    // 1. 발주 리스트 조회
    @GetMapping("/procurement")
    public Map<String, Object> getProcurement(Pageable pageable,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                              @RequestParam(required = false) String searchType,
                                              @RequestParam(required = false) String keyword) {

        Page<PurchaseOrderDTO> page =
                purchaseOrderService.getPurchaseOrderList(pageable, startDate, endDate, searchType, keyword);

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> pagination = new HashMap<>();

        pagination.put("page", page.getNumber() + 1); // 1부터 시작
        pagination.put("totalCount", page.getTotalElements());

        data.put("contents", page.getContent());
        data.put("pagination", pagination);

        response.put("result", true);
        response.put("data", data);

        return response;
    }

    // 2. 발주 상세 조회
    @GetMapping("/procurement_detail")
    public PurchaseOrderDetailDTO getProcurementDetail(@RequestParam("purchaseOrderId") Long purchaseOrderId){

        log.info("발주 상세 조회 - 발주 ID: {}", purchaseOrderId);

        PurchaseOrderDetailDTO detailDTO = purchaseOrderService.getPurchaseOrderDetail(purchaseOrderId);

        log.info("발주 상세 조회 - 결과: {}", detailDTO);

        return detailDTO;
    }

    // 3. 신규 발주 등록
    @PostMapping("/procurement/register")
    public void saveProcurement(@RequestBody PurchaseOrderInsertDTO purchaseOrderInsertDTO,
                                @AuthenticationPrincipal AccountLogin accountLogin){

        log.info("발주 등록 리스트 조회 - 결과: {}", purchaseOrderInsertDTO);

        Long userId = accountLogin.getId();

        purchaseOrderService.savePurchaseOrder(purchaseOrderInsertDTO, userId);
    }


    // 4. 배송 -> 품질검사 처리


    // 5. 반품 조회
    @GetMapping("procurement/return")
    public Page<PurchaseReturnDTO> getProcurementReturn(Pageable pageable){

        Page<PurchaseReturnDTO> list = purchaseReturnService.getReturnPurchase(pageable);

        return list;
    }

    // 6. 반품처리
    @PostMapping("procurement/return_insert")
    public void returnProcurement(@RequestBody PurchaseReturnInsertDTO purchaseReturnInsertDTO,
                                  @AuthenticationPrincipal AccountLogin accountLogin){

        Long userId = accountLogin.getId();

        purchaseReturnService.returnPurchase(purchaseReturnInsertDTO, userId);

    }

    // 7. 검수 완료 처리
    @PostMapping("procurement/complete_qc")
    public void completeQcProcurement(@RequestParam(required = true) Long purchaseOrderId){
        purchaseOrderService.completeQcPurchase(purchaseOrderId);
    }
}