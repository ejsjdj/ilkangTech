package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderNeededItemDTO { // 발주 필요 리스트용
    private Long itemId;
    private String itemName;
    private Long currentStock; // 현재고
    private Long incomingStock; // 입고예정 (더미)
    private Long productionPlan; // 생산계획 (더미)
    private Long reservedStock; // 예약재고 (더미)
    private Long safeStock; // 안전재고 (더미)
    private Long requiredStock; // 필요재고 (더미 계산)
    private String uom; // 단위
}