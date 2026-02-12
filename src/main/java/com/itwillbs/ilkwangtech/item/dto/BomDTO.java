package com.itwillbs.ilkwangtech.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BomDTO {

    Integer idx;

    // 이전 제품의 외래키
    Integer before;
    // 이후 제품의 외래키
    Integer after;

    Integer quantity;

}
