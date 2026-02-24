package com.itwillbs.ilkwangtech.production.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

// 생산계획 상세 엔티티
@Entity
public class ProductionPlaneDetailEntity {

    private Long id;

    private Long planeId; // 헤더 ID와 FK로 연결

    private LocalDateTime planeDetailDate;

    private String member;

    private String status;

    private String memo;

    private Long orderId; // 수주ID와 FK로 연결(수주코드, 거래처명, 주문수량, 수주일자, 납기일)

    private Long productQty; // 수주별 계획 수량

    private Long LeftQty; // 수주별 남은 수량

}
