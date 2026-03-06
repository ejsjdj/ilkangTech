package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OutboundItemDTO {
    private String type;          // 구분 ("자재출고" or "제품출고") -> (출고 사유)
    private String refCode;       // 관련 문서번호 (작업지시번호 등)
    private Long itemCode;      // 품목코드
    private String itemName;      // 품목명    
    private String requestDept;   // 요청팀 (생산팀/영업팀 등)
    private String dueDate;       // 출고일
    private String status;        // 상태 (출고 대기 / 재고 부족)
    
    private Long requiredQty;     // 필요(출고) 수량
    private Long currentStock;    // 재고
    
    private Long availableOutboundQty;  // 실제 출고(차감)될 수량
    private Long shortageQty;			// 부족 수량 (예: 1500)
}