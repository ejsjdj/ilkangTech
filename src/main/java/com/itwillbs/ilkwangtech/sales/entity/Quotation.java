package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.sales.constant.QuotationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotations")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "QUOTATIONS_SEQ_GENERATOR",
        sequenceName = "QUOTATIONS_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATIONS_SEQ_GENERATOR")
    @Column(updatable = false)
    private Long quotationId;

    // 견적서 번호 (QT-20260202-001)
    @Column(unique = true, nullable = false, length = 50)
    private String quotationNo;

    // 고객 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // 견적 일자
    @Column(nullable = false)
    private LocalDate quotationDate;

    // 견적 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuotationStatus status = QuotationStatus.DRAFT;

    // 납기일/리드타임
    private LocalDateTime leadTime;

    // 금액 정보
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // 통화 (KRW, USD 등)
    @Column(length = 10)
    private String currency = "KRW";

    // 비고
    @Column(length = 255)
    private String remarks;

    // 담당 영업사원
    @Column(length = 50)
    private String salesPerson;

    // 견적서 상세 품목들 (1:N 관계)
    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuotationDetail> details = new ArrayList<>();

    // 주문 추가 //
    public void addDetail(QuotationDetail detail) {
        details.add(detail);
        detail.setQuotation(this);
    }

    // 총액 계산
    public void calculateTotals() {
        this.subtotal = details.stream()
                .map(QuotationDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.taxAmount = subtotal.multiply(new BigDecimal("0.1")); // 10% 부가세
        this.totalAmount = subtotal.add(taxAmount);
    }
}
