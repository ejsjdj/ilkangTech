package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALES_ORDERS_SEQ_GENERATOR")
    private Long salesOrderId;

}
