package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
