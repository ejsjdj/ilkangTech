package com.itwillbs.ilkwangtech.standard.entity;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemCode;

    private String itemName;

    @Column(name = "item_type2")
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String uom;

    private Long standardPrice;
}
