package com.itwillbs.ilkwangtech.Hr.constant;

import lombok.Getter;

@Getter
public enum AttendanceStatus {
	
	// DB에 저장될 값과 화면에 보여줄 값을 연결하는 객체
	ON_DUTY("출근"),        // 출근 상태
    OFF_DUTY("퇴근"),       // 퇴근 완료
    OUT_WORK("외근"),       // 외근 중
    LEAVE("장기휴식"),      // 10분 이상 장기 휴식
    RETURN("복귀");         // 복귀
	
	private final String description;
	
	AttendanceStatus(String description) {
        this.description = description;
    }

}
