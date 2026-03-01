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
    @JoinColumn(name = "parent_item_id")
    private ItemEntity parentItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_item_id")
    private ItemEntity childItem;

    private Long requireQty;

}
