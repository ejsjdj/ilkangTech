package com.itwillbs.ilkwangtech.item.dto;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemDTO {

    Integer id;
    String itemCode;

    // getLabel() -> 원자재, 중간제품, 완제품
    // getId() -> 1, 2, 3
    ItemType type;

    Integer company_id;

    String description;

}