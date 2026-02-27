package com.itwillbs.ilkwangtech.inventorymg.entity;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_history")
@Getter
@Setter
public class InventoryHistoryEntity { // 재고 수불 이력 - 그래프용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "transaction_date")
    private LocalDate transactionDate; // 발생 일자

    @Column(name = "transaction_type")
    private String transactionType; // IN(입고), OUT(출고), DISCARD(폐기)

    @Column(name = "quantity")
    private Long quantity; // 변동 수량
}