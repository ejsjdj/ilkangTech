package com.itwillbs.ilkwangtech.hr.dto;

import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AppointmentRegistDTO {

    // ================= 발령 등록 필드 =================//
    private Long memberId; //1. 발령 대상자 ID
    private Integer newDept; //3. 새 부서
    private Integer newRank; //5. 새 직급
    private String workStatus; //6. 근무 상태
    private String approveStatus; //7. 승인 상태



    // private LocalDate appointmentDate; // 승인일
    // private Member approverId; // 승인자 ID
    // private DraftEntity draft; // 결재 ID
}
