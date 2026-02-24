package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderLineDTO {

    private Long id;
    private Long item;
    private Long quantity;
    private Long unitPrice;

    public PurchaseOrderLineDTO(Long id, Long item, Long quantity, Long unitPrice){
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
