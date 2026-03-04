package com.itwillbs.ilkwangtech.process.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProcessStatusResponseDTO {
    private String instructCode;    // 작업지시번호
    private String itemName;            // 제품명
    private Integer instructQty;    // 계획수량
    private Integer defective;      // 불량수량
    private String status;          // 상태
    private String operationName;     // 현재공정
    private LocalDateTime startDate; // 시작일시 (경과시간 계산용)
    private LocalDateTime endDate;   // 종료/목표일시
    
    // 비즈니스 로직으로 계산된 생산량 (계획 - 불량)
    public Integer getProductionQty() {
        return (instructQty != null ? instructQty : 0) - (defective != null ? defective : 0);
    }
}
