package com.itwillbs.ilkwangtech.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MemberDeptRowDTO {

	private final Long memberId;
	private final String memberName;
    private final String departmentName;

}
