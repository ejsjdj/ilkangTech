package com.itwillbs.ilkwangtech.standard.dto;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BomDTO {

    private Long bomId;

    private ItemEntity beforeItemId;

    private ItemEntity afterItemId;

    private Long requiredQty;

}
