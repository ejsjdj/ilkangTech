package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// 발주 라인 엔티티
@Entity
@Getter
@Setter
@Table(name = "purchase_order")
public class PurchaseOrderEntity {

    // 발주 ID
    @Id
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 발주 번호
    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrderHeaderEntity header;

    // 품목명
    @Column(name = "item_id")
    private Long item;

    // 수량
    @Column(name = "quantity")
    private Long quantity;

    // 단가
    @Column(name = "unit_price")
    private Long unitPrice;
}