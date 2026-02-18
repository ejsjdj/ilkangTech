package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequestDTO {

    private Long id; // 기본키
    private String purchaseId; // 구매코드
    private String itemId; // 품목코드
    private String contractType; // 계약구분
    private String amount; // 수량
    private String unit; // 단위
    private LocalDate dueDate; // 납기일
    private String dlvLocate; //  납품장소

}