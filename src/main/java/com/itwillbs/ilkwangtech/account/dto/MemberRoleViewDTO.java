package com.itwillbs.ilkwangtech.account.dto;

import lombok.Getter;

/**
 * 권한별 사원 목록 조회 시 사용하는 Projection용 DTO
 */
@Getter
public class MemberRoleViewDTO {

    private Long memberId;      // 사원 고유 ID
    private Long roleId;        // 권한 고유 ID
    private String name;        // 이름
    private String employeeNumber; // 사원번호
    private String department;  // 부서명
    private String position;    // 직급명
    private String description; // 권한 상세 설명 (공통코드명)

    public MemberRoleViewDTO(Long memberId, Long roleId, String name, String employeeNumber, String department, String position, String description) {
        this.memberId = memberId;
        this.roleId = roleId;
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.department = department;
        this.position = position;
        this.description = description;
    }

}
