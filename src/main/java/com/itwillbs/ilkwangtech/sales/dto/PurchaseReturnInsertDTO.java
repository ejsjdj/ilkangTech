package com.itwillbs.ilkwangtech.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseReturnInsertDTO {

    private Long purchaseRequestDetailId;
    private Long returnQty;
    private String memo;

}
