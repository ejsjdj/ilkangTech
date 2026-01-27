package com.itwillbs.ilkwangtech.account.dto;

import lombok.*;

/**
 * 사원 목록 조회 시 사용하는 간략한 사원 정보 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDetail {

    private Long id;                // 고유 ID

    private String employeeNumber;  // 사원번호

    private String name;            // 이름

    private String department;      // 부서명

    private String position;        // 직급명

    private String status;          // 재직 상태

    private String phoneNumber;     // 전화번호

    private String email;           // 이메일

}
