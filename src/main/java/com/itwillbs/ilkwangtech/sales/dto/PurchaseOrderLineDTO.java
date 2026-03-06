package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderLineDTO {

    private Long id;
    private Long item;
    private String itemName;
    private Long quantity;
    private Long totalPrice;

    public PurchaseOrderLineDTO(Long id, Long item, String itemName, Long quantity, Long totalPrice){
        this.id = id;
        this.item = item;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
