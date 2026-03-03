package com.itwillbs.ilkwangtech.production.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequirementDTO {
    private Long itemId;          // 품목 ID
    private String itemName;     // 품목명
    private Long stockQty;       // 현재고
    private Long incomingQty;    // 입고예정
    private Long outgoingQty;    // 출하예정
    private Long availableQty;   // 가용량 (계산 필드)
}