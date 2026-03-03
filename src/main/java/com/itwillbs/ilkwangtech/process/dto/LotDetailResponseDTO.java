package com.itwillbs.ilkwangtech.process.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LotDetailResponseDTO {
    private String lotId;           // LOT 코드
    private String itemName;        // 제품명
    private String instructCode;    // 작업지시번호
    private Integer instructQty;    // 생산 수량
    private String status;          // 생산 상태
    private LocalDateTime startDate; // 시작일
    private LocalDateTime endDate;   // 종료일
    private Integer defective;      // 불량 수량
}
