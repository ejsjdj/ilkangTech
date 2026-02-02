package com.itwillbs.ilkwangtech.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDeptRowDTO {

	private Long memberId;
	private String memberName;
    private String departmentName;
    private String isFavorite = "N";

    public MemberDeptRowDTO(Long memberId, String memberName, String departmentName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.departmentName = departmentName;
        this.isFavorite = "N"; // 기본값 설정
    }
    
}
