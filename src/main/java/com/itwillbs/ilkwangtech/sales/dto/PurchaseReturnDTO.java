package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseReturnEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PurchaseReturnDTO {

    private Long id;
    private String orderCode;
    private String itemName;
    private Long returnQty;
    private String memberName;
    private String status;
    private String returnDate;

    public static PurchaseReturnDTO fromList(PurchaseReturnEntity entity){
        return PurchaseReturnDTO.builder().
                id(entity.getId()).
                orderCode(entity.getHeader().getHeader().getPurchaseOrderCode()).
                itemName(entity.getHeader().getItem().getItemName()).
                returnQty(entity.getReturnQty()).
                memberName(entity.getMember().getName()).
                status(entity.getStatus()).
                returnDate(String.valueOf(entity.getReturnDate())).
                build();
    }

}
