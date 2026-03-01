package com.itwillbs.ilkwangtech.inventorymg.entity;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "lot_number", unique = true)
    private String lotNumber; // LOT 번호

    @Column(name = "zone")
    private String zone; // 창고 구역 (ZONE A, B, C)

    @Column(name = "rack")
    private String rack; // 랙 번호 (Rack 01, 02, 03)

    @Column(name = "current_quantity")
    private Long currentQuantity; // 현재 수량

    @Column(name = "expiration_date")
    private LocalDate expirationDate; // 유통기한 (임박 재고 파악용)
}
