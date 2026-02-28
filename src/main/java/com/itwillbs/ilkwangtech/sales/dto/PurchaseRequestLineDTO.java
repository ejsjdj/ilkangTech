package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseRequestLineDTO {

    private Long id;
    private Long itemId;
    private String itemName;
    private Long quantity;
    private String uom;
    private Long price;

}
