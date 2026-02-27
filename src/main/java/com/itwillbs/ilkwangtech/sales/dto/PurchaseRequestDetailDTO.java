package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class PurchaseRequestDetailDTO {

    private Long id;
    private String purchaseRequestCode;
    private String memberName;
    private LocalDate requestDate;
    private LocalDate dueDate;
    private String deliveryLocate;
    private String contractType;
    private String produceType;
    private List<PurchaseRequestLineDTO> lines;

}
