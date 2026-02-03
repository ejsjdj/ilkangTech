package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WAREHOUSES")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "WAREHOUSES_SEQ_GENERATOR",
        sequenceName = "WAREHOUSES_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
public class WareHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSES_SEQ_GENERATOR")
    private Long warehouseId;

    // 창고의 이름(건조기 출하창고, 세탁기 출하창고)
    @Column(length = 100)
    private String warehouseName;

    // 창고에 보관중인 제품과 해당 제품의 재고량
    @OneToMany(mappedBy = "warehouse")
    private List<ItemStock> itemStocks = new ArrayList<>();

}
