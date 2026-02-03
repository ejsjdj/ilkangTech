package com.itwillbs.ilkwangtech.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "ITEMS_SEQ_GENERATOR",
        sequenceName = "ITEM_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
/**
 * 제품이나 재공품, 원자재등의 정보를 저장할 엔티티 클래스
 *
 */
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_SEQ_GENERATOR")
    @Column(updatable = false)
    private long itemId;

    private String itemCode;
    private String itemName;

    // 원자재인지, 재공품인지, 완제품인지
    private Integer itemType;

    // 이 품목이 하위 자재를 가질때 해당하는 아이템들을 기록하기 위한 리스트
    @OneToMany(mappedBy = "parentItem")
    private List<ItemBom> childItems;

    // 이 품목이 어떤 제품의 재료일때 그에 해당하는 아이템을 기록할 리스트
    @OneToMany(mappedBy = "childItem")
    private List<ItemBom> parentItems;

}