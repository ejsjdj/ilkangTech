package com.itwillbs.ilkwangtech.messenger.dto;

public class MemberDeptRowDTO {

	private final String memberName;
    private final String departmentName;

    public MemberDeptRowDTO(String memberName, String departmentName) {
        this.memberName = memberName;
        this.departmentName = departmentName;
    }

    public String getMemberName() { return memberName; }
    public String getDepartmentName() { return departmentName; }
}
