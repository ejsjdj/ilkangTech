package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.CustomerStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerDTO {
    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private CustomerStatus valid;

}