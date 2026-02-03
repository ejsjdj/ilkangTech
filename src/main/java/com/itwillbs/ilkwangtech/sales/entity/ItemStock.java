package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ItemStock")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "ITEMSTOCKS_SEQ_GENERATOR",
        sequenceName = "ITEMSTOCKS_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
public class ItemStock {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMSTOCKS_SEQ_GENERATOR")
    private Long itemStockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ware_house_id")
    private WareHouse warehouse;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 50)
    private String productCode;

    @Column(length = 20)
    private String unit; // 개, 박스, 팔레트 등

    @Column(length = 500)
    private String description;

    // 현재 기본 단가 (고객사별 협상 전 기준 단가)
    @Column(precision = 15, scale = 2)
    private BigDecimal currentUnitPrice;

}
