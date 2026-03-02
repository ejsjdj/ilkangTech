package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InboundItemDTO {
    private String purchaseOrderCode; // 발주 코드
    private String company;           // 거래처명
    private String itemCode;          // 품목 코드
    private String itemName;          // 품목명
    private Long quantity;            // 수량

}