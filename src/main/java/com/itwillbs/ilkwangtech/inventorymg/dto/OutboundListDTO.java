package com.itwillbs.ilkwangtech.inventorymg.dto; // (패키지 경로는 프로젝트에 맞게 수정)

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboundListDTO {
    private String itemType;      // 품목 구분 (원자재, 반제품, 완제품)
    private String lotNumber;     // LOT 번호
    private String itemName;      // 제품명
    private Long outboundQty;     // 출고량
    private String outboundDate;  // 출고일
    private String requestDept;   // 출고팀 (생산팀/영업팀)
}