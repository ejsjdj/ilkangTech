package com.itwillbs.ilkwangtech.account.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDetailResponse {
    private Long id;
    private String employeeNumber;
    private String name;
    private int gender;
    private LocalDate hireDate;
    private String residentNumber;
    private String email;
    private String phoneNumber;
    private String department;
    private String position;
    private String bank;
    private String accountNumber;
    private String profilePhotoLink;
    private List<String> roles;
    private String status;
}
