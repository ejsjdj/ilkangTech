package com.itwillbs.ilkwangtech.production.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

// 생산계획 상세 엔티티
@Entity
@Getter
@Table(name = "production_plane_detail")
public class ProductionPlaneDetailEntity {

    // 생산 상세 ID
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 생산 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plane_id")
    private ProductionPlaneEntity header; // 생산헤더 ID와 FK로 연결

    // 수주 ID
    @Column(name = "plane_detail_date")
    private Long orderId; // 수주ID와 FK로 연결(수주코드, 거래처명, 주문수량, 수주일자, 납기일)

    // 수주별 계획 수량
    @Column(name = "product_qty")
    private Long productQty;

    // 메모
    @Column(name = "memo")
    private String memo;

}
