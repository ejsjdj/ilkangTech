package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestDTO;
import com.itwillbs.ilkwangtech.sales.service.purchaseRequest.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales/purchaseRequest")
@RequiredArgsConstructor
public class PurchaseRequestApiController {

    private final PurchaseRequestService purchaseRequestService;

    // 구매 요청 등록
    @PostMapping("/create")
    public String createPurchaseRequest(@RequestBody PurchaseRequestDTO purchaseRequestDTO){

        purchaseRequestService.createPurchaseRequest(purchaseRequestDTO);

        return "구매요청 등록을 완료했습니다.";
    }
}
