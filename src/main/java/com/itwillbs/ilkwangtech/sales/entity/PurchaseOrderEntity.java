package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// 발주 라인 엔티티
@Entity
@Getter
@Setter
@Table(name = "purchase_order")
public class PurchaseOrderEntity {

    // 발주라인 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_seq")
    @SequenceGenerator(name = "return_seq", sequenceName = "PURCHASE_RETURN_SEQ", allocationSize = 1)
    private Long id;

    // 발주헤더 ID (FK)
    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrderHeaderEntity header;

    // 품목
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    // 수량
    @Column(name = "quantity")
    private Long quantity;

    // 단가
    @Column(name = "unit_price")
    private Long unitPrice;

    // 발주상세 등록
    public static PurchaseOrderEntity create(ItemEntity item,
                                             Long quantity,
                                             Long unitPrice){

        PurchaseOrderEntity line = new PurchaseOrderEntity();

        line.item = item;
        line.quantity = quantity;
        line.unitPrice = unitPrice;

        return line;
    }
}