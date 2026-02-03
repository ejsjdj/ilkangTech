package com.itwillbs.ilkwangtech.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_boms")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "BOM_SEQ_GENERATOR",
        sequenceName = "BOM_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
/**
 * 제품이나 재공품, 원자재등의 정보를 저장할 엔티티 클래스
 *
 */
public class ItemBom {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOM_SEQ_GENERATOR")
    private Long bomId;

    // 상위 품목(완제품 또는 반제품)
    @ManyToOne
    @JoinColumn(name = "parent_item_id")
    private Item parentItem;

    // 하위 품목(구성 자재)
    @ManyToOne
    @JoinColumn(name = "child_item_id")
    private Item childItem;

    // 상위 품목을 만들기 위해 필요한 하위 품목의 필요한 수량
    private Integer quantity;
}
