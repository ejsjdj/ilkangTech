package com.itwillbs.ilkwangtech.production.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockRequirementDTO {

    private Long itemId;
    private String itemName;
    private Long required_qty;

    public StockRequirementDTO(Long itemId, Long required_qty){
        this.itemId = itemId;
        this.required_qty = required_qty;
    }
}
