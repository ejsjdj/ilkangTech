package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALES_ORDERS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "SALES_ORDERS_SEQ_GENERATOR",
        sequenceName = "SALES_ORDERS_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
/**
 * 판매 주문 엔티티
 */
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALES_ORDERS_SEQ_GENERATOR")
    private Long salesOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_stock_id")
    private ItemStock itemStock;

    @Column(nullable = false, length = 50)
    private String orderNumber;

    @Column(nullable = false, length = 20)
    private String status; // REQUESTED, ACCEPTED, REJECTED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "acceptance_date")
    private LocalDateTime acceptanceDate;

    @Column(name = "delivery_deadline")
    private LocalDateTime deliveryDeadline;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Column(length = 500)
    private String customerRequest;

    @Column(length = 500)
    private String notes;

    @Column(length = 50)
    private String salesPerson;

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 20)
    private String contactPhone;

}
