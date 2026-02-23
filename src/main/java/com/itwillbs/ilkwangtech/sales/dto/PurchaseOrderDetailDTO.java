package com.itwillbs.ilkwangtech.sales.dto;


import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class PurchaseOrderDetailDTO {


    private List<PurchaseOrderLineDTO> purchaseOrderLineDto;

    private String purchaseOrderCode; // 발주 코드

    private String company;// 거래처명

    private String companyManager;// 담당자명

    private String phone;// 전화번호

    private String name;// 발주 담당자

    private String status;// 발주 상태(발주접수, 발주확정, 출하완료, 검수완료, 입고완료)

    private String orderDate;// 발주 일자

    private Long amount; // 총 금액
}
