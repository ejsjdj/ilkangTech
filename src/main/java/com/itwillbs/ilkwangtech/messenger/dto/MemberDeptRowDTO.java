package com.itwillbs.ilkwangtech.messenger.dto;

public class MemberDeptRowDTO {

	private final Long memberId;
	private final String memberName;
    private final String departmentName;

    public MemberDeptRowDTO(Long memberId, String memberName, String departmentName) {
        this.memberId = memberId;
		this.memberName = memberName;
        this.departmentName = departmentName;
    }

    public Long getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public String getDepartmentName() { return departmentName; }
}
