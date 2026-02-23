package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 구매요청 라인 엔티티
@Entity
@Getter
@Setter
@Table(name = "purchase_request")
public class PurchaseRequestEntity {

    // 구매요청 라인 ID
    @Id
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구매요청 헤더 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_request_id")
    private PurchaseRequestHeaderEntity header;

    // 품목 ID
    @Column(name = "item_id")
    private Long itemId;

    // 수량
    @Column(name = "quantity")
    private Long quantity;

}