package com.itwillbs.ilkwangtech.sales.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderDTO {
    // 발주번호
    private String purchaseId;
    // 거래처명
    private String company;
    // 발주일자
    private String purchaseDate;
    // 납기일자
    private String DeliveryDate;
    // 상태
    private String status;
    // 담당자
    private String memberName;
}
