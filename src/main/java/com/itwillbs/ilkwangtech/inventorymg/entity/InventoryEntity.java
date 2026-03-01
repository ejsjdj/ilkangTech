package com.itwillbs.ilkwangtech.inventorymg.entity;

import java.time.LocalDate;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class InventoryEntity { // 실물 재고 관리

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
