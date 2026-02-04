package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "WAREHOUSE_TRANSACTIONS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "WAREHOUSE_TRANSACTIONS_SEQ_GENERATOR",
        sequenceName = "WAREHOUSE_TRANSACTIONS_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
/**
 * 창고 입출고 기록 엔티티
 */
public class WarehouseTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_TRANSACTIONS_SEQ_GENERATOR")
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private WareHouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_stock_id")
    private ItemStock itemStock;

    @Column(nullable = false, length = 20)
    private String transactionType; // IN, OUT, ADJUSTMENT

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer beforeQuantity;

    @Column(nullable = false)
    private Integer afterQuantity;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(length = 100)
    private String referenceNumber; // 주문번호, 생산번호 등

    @Column(length = 500)
    private String description;

    @Column(length = 50)
    private String operator; // 작업자

}
