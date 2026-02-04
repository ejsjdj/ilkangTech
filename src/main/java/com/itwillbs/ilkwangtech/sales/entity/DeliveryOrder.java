package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "DELIVERY_ORDERS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "DELIVERY_ORDERS_SEQ_GENERATOR",
        sequenceName = "DELIVERY_ORDERS_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
/**
 * 출고 지시 정보를 저장하는 엔티티
 *
 */
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DELIVERY_ORDERS_SEQ_GENERATOR")
    private Long deliveryOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_stock_id")
    private ItemStock itemStock;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(length = 255)
    private String deliveryAddress;

    @Column(length = 100)
    private String trackingNumber;

    @Column(length = 500)
    private String notes;

}
