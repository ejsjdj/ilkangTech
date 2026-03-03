package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OutboundItemDTO {
    private String type;          // 구분 ("자재출고" or "제품출고")
    private String refCode;       // 관련 문서번호 (작업지시번호 등)
    private String itemCode;      // 품목코드
    private String itemName;      // 품목명
    private Long requiredQty;     // 필요(출고) 수량
}