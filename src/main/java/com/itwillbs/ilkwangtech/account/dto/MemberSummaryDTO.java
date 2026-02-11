package com.itwillbs.ilkwangtech.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 권한 부여 시 검색 결과로 보여줄 사원 요약 정보 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSummaryDTO {
    private Long id;
    private String name;
    private String employeeNumber;
    private String departmentName;
    private String positionName;
}
