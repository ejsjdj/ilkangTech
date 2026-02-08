package com.itwillbs.ilkwangtech.account.dto;

import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.*;

/**
 * 사원 목록 조회 시 사용하는 간략한 사원 정보 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AccountForList {

    private Long id;                // 고유 ID

    private String employeeNumber;  // 사원번호

    private String name;            // 이름

    private String department;      // 부서명

    private String position;        // 직급명

    private String status;          // 재직 상태

    private String phoneNumber;     // 전화번호

    private String email;           // 이메일

    public static AccountForList of(Member member, String departmentName, String positionName) {
        return AccountForList.builder()
                .id(member.getId())
                .employeeNumber(member.getEmployeeNumber())
                .name(member.getName())
                .department(departmentName)
                .position(positionName)
                .status(member.getStatus().getDescription())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .build();
    }
}
