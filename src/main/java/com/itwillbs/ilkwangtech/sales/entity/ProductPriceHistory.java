package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_PRICE_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "PRODUCT_PRICE_HISTORY_SEQ_GENERATOR",
        sequenceName = "PRODUCT_PRICE_HISTORY_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
/**
 * 제품 단가 변경 이력을 관리하는 엔티티
 */
public class ProductPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_PRICE_HISTORY_SEQ_GENERATOR")
    private Long priceHistoryId;

    // 제품 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_stock_id", nullable = false)
    private ItemStock itemStock;

    // 고객사 정보 (고객사별 단가 협상을 위함)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // 단가 정보
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    // 이전 단가
    @Column(precision = 15, scale = 2)
    private BigDecimal previousPrice;

    // 단가 적용 시작일
    @Column(nullable = false)
    private LocalDateTime effectiveDate;

    // 단가 적용 종료일 (null이면 현재까지 적용 중)
    private LocalDateTime endDate;

    // 단가 변경 사유
    @Column(length = 500)
    private String changeReason;

    // 등록자
    @Column(length = 50)
    private String registeredBy;

    // 생성일
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 생성자
    public ProductPriceHistory(ItemStock itemStock, Customer customer, BigDecimal unitPrice, 
                              BigDecimal previousPrice, String changeReason, String registeredBy) {
        this.itemStock = itemStock;
        this.customer = customer;
        this.unitPrice = unitPrice;
        this.previousPrice = previousPrice;
        this.effectiveDate = LocalDateTime.now();
        this.changeReason = changeReason;
        this.registeredBy = registeredBy;
        this.createdAt = LocalDateTime.now();
    }

    // 단가 종료 처리
    public void endPrice() {
        this.endDate = LocalDateTime.now();
    }
}
