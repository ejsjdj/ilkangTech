package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestHeaderDTO;
import com.itwillbs.ilkwangtech.sales.service.purchaserequest.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class PurchaseRequestApiController {

    private final PurchaseRequestService purchaseRequestService;
    // 구매요청 조회
    @GetMapping("/purchase_request_list")
    public List<PurchaseRequestHeaderDTO> getPruchaseRequest(){

        List<PurchaseRequestHeaderDTO> list = purchaseRequestService.getPruchaseRequest();;

        return list;
    }


    // 구매요청 상세조회
    @GetMapping("/purchase_request_detail")
    public PurchaseRequestDetailDTO getPruchaseRequestDetail(@RequestParam(required = true) Long requestId){

        System.out.println("구매요청 상세조회 실행됨!!!!!!!! : " + requestId);

        PurchaseRequestDetailDTO detail = purchaseRequestService.getPurchaseReuqestDetail(requestId);;

        return detail;
    }

}
