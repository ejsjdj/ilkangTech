package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderInsertDTO {

    private List<PurchaseOrderLineDTO> lines;
    private String purchaseOrderCode;
    private String company;
    private String companyManager;
    private String phone;
    private String status;
    private Long amount;

}
