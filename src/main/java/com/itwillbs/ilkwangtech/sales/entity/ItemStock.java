package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ItemStock")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "ITEMSTOCK_SEQ_GENERATOR",
        sequenceName = "ITEMSTOCK_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
public class ItemStock {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMSTOCK_SEQ_GENERATOR")
    private Long ItemStockId;

}
