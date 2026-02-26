package com.itwillbs.ilkwangtech.production.dto;

import java.time.LocalDate;

public class ProductionInstructDetailDTO {

    // Lot 번호
    private String lotCode;
    // 생산계획코드
    private String planeId;
    // 품목명
    private Long item;
    // 생산일자
    private LocalDate instructDate;
    // 계획수량
    private Long instructQty;
    // 공정코드
    private String operationCode;
    //부서
    private String department;
    // 담당 작업자
    private String memberName;
    // 상태
    private String status;

}