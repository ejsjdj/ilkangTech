package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderNeededItemDTO {
    private Long itemId;
    private String itemName;
    private Long currentStock; // 현재고
    private Long incomingStock; // 입고예정 (더미)
    private Long productionPlan; // 생산계획 (더미)
    private Long reservedStock; // 예약재고 (더미)
    private Long safeStock; // 안전재고 (더미)
    private Long requiredStock; // 필요재고 (더미 계산)
    private String uom; // 단위
    private String status; // "발주대기" 또는 "요청완료"
    private Long pendingRequestQty; // 현재 구매요청(PR) 진행 중인 수량
}