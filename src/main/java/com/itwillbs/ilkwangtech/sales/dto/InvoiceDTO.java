package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceDTO {

    Long InvoiceId;
    Long customerId;
    List<Long> productId;

}
