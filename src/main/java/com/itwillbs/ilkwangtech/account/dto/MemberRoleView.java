package com.itwillbs.ilkwangtech.account.dto;

import lombok.Getter;

@Getter
public class MemberRoleView {

    private Long memberId;
    private Long roleId;
    private String name;
    private String employeeNumber;
    private String department;
    private String position;
    private String description;

    public MemberRoleView(Long memberId, Long roleId, String name, String employeeNumber, String department, String position, String description) {
        this.memberId = memberId;
        this.roleId = roleId;
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.department = department;
        this.position = position;
        this.description = description;
    }

}
