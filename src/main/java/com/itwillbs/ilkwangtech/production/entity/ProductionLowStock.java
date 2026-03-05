package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "production_low_stock")
public class ProductionLowStock {

    // PK
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 품목명
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    // 부족 수량
    @Column(name = "required_qty")
    private Long requiredQty;
}
