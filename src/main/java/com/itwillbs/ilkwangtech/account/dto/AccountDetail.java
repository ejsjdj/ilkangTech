package com.itwillbs.ilkwangtech.account.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDetail {

    private Long id;

    private String employeeNumber;

    private String name;

    private String department;

    private String position;

    private String status;

    private String phoneNumber;

    private String email;

}
