package com.itwillbs.ilkwangtech.sales.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerDTO {

    private Long customerId;
    private String customerName;
    private String businessNumber;
    private String representativeName;
    private String phoneNumber;
    private String faxNumber;
    private String address;
    private String email;
    private String contactPerson;
    private String remark;

}
