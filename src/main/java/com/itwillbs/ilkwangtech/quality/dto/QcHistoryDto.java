package com.itwillbs.ilkwangtech.quality.dto;

import java.time.LocalDateTime;

public interface QcHistoryDto {
    Long getWorkerId();          // [추가] 생산 실적 ID (사유 입력 시 매칭용)
    String getInstructCode();    // 작업지시번호
    String getItemName();        // 제품명
    LocalDateTime getEndTime();  // 검사일자
    String getMemberName();      // 검사자
    Integer getProductionQty();  // 생산수량
    Integer getDefectiveQty();   // 불량수량
    String getRejectReason();    // [수정됨] 불합격사유 (테이블에서 가져옴)
}