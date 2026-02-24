package com.itwillbs.ilkwangtech.production.entity;

import jakarta.persistence.Entity;

import java.time.LocalDate;

// 생산 계획 엔티티
@Entity
public class ProductionPlaneEntity {

    private Long id;

    private String planeCode;

    private LocalDate planeDate;

    private Long item; // 제품ID와 FK로 연결(제품코드, 제품명)

    private Long totalQty; // 계획 총수량

    private Long totalLeftQty; // 계획 잔여 수량

    private String status;

    private String memo;
}
