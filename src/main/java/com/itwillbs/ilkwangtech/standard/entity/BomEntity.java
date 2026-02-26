package com.itwillbs.ilkwangtech.standard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BOM")
@Getter
@Setter
public class BomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "before_item_id")
    private ItemEntity beforeItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_item_id")
    private ItemEntity afterItemId;

    private Long requiredQty;

}
