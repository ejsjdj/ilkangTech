package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryListDTO {
    private Long id;                // 재고 고유 ID
    private String lotNumber;       // LOT 번호
    private String itemName;        // 제품명
    private Long currentQuantity;   // 재고량
    private Long scheduledOutbound; // 출고예정 (임시 0 처리)
    private String location;        // 위치 (Zone + Rack)
    private String inboundDate;     // 입고일 (임시로 오늘 날짜 처리)
    private String itemType;        // 품목 타입 (ALL, RAW, SEMI, FINISHED 필터용)
}