package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.CustomerCategory;
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
    private String manager;
    private CustomerCategory category;
    private CustomerStatus valid;

}