package com.itwillbs.ilkwangtech.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class LoginAttemptDTO {
    private Long loginAttemptId;

    private Long memberId;

    private String employeeNumber;

    private String department;

    private String position;

    private String name;

    private String phoneNumber;

    private String email;

    private LocalDateTime lockDateTime;
}
