package com.itwillbs.ilkwangtech.item.dto;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias(value = "item")
public class ItemDTO {

    Integer id;

    String itemCode;

    // getLabel() -> 원자재, 재공품, 제품
    // getId() -> 1, 2, 3
    ItemType type;

    String desription;

}