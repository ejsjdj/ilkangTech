package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "quotation_details")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "QUOTATION_DETAILS_SEQ_GENERATOR",
        sequenceName = "QUOTATION_DETAILS_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
public class QuotationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATION_DETAILS_SEQ_GENERATOR")
    private Long quotationDetailId;

    // 견적서 헤더
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    private Quotation quotation;

    // 품목
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // 수량
    @Column(nullable = false)
    private Integer quantity;

    // 단가
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    // 금액 (수량 × 단가)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    // 최소 주문 수량(MOQ)
    private Integer moq;

    // 품목별 비고
    @Column(length = 500)
    private String remarks;

    // 금액 자동 계산
    public void calculateAmount() {
        if (quantity != null && unitPrice != null) {
            this.amount = unitPrice.multiply(new BigDecimal(quantity));
        }
    }
}
