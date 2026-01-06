package com.itwillbs.ilkwangtech.account.dto;

import java.time.LocalDate;

public class AccountDTO {

    public record JoinRequest(
            String name,
            LocalDate joinDate,
            LocalDate birthDate,
            String phoneNumber,
            String gender,
            String email,
            String position,    // 직급
            String department   // 부서
    ) {}

    public record JoinResponse(
            Long id,
            String name,
            String email,
            LocalDate joinDate
    ) {}

}
