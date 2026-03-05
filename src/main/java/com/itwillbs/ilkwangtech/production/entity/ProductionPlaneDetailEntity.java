package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 생산계획 상세 엔티티
@Entity
@Getter
@Setter
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

    // 품목 ID
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    // 수주 ID
    @Column(name = "order_id")
    private Long orderId; // 수주ID와 FK로 연결(수주코드, 거래처명, 주문수량, 수주일자, 납기일)

    // 수주별 계획 수량
    @Column(name = "product_qty")
    private Long productQty;

    // 메모
    @Column(name = "memo")
    private String memo;

    // 예상 생산 완료일
    @Column(name = "plane_detail_date")
    private LocalDateTime planeDetailDate;


    public static ProductionPlaneDetailEntity create(
            ItemEntity item,
            Long orderId,
            Long productQty,
            String memo){
        ProductionPlaneDetailEntity detail = new ProductionPlaneDetailEntity();

        detail.item = item;
        detail.orderId = orderId;
        detail.productQty = productQty;
        detail.memo = memo;
        detail.planeDetailDate = LocalDateTime.now();

        return detail;
    }

}
