package com.itwillbs.ilkwangtech.inventorymg.dto; // 패키지명 확인

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistoryDTO {
    private String transactionDate; // 변동 일자
    private String transactionType; // 변동 유형 (입고/출고/폐기)
    private String itemType;        // 품목 구분 (원자재/반제품/완제품)
    private String itemName;        // 품목명
    private Long quantity;          // 변동 수량
}