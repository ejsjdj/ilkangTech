package com.itwillbs.ilkwangtech.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// 발령리스트 조회 DTO
@Getter
@Setter
@NoArgsConstructor
public class AppointmentDTO {

    private Long appointmentId;
    private String memberId;
    private String preDept;
    private String currentDept;
    private String preRank;
    private String currentRank;
    private String workStatus;
    private String approveStatus;
    private LocalDate approveDate;

    @Builder
    public AppointmentDTO(Long appointmentId, String memberId, String preDept, String currentDept, String currentRank, String preRank, String workStatus, String approveStatus, LocalDate approveDate){
        super();
        this.appointmentId = appointmentId;
        this.memberId = memberId;
        this.preDept = preDept;
        this.currentDept = currentDept;
        this.preRank = preRank;
        this.currentRank = currentRank;
        this.workStatus = workStatus;
        this.approveStatus = approveStatus;
        this.approveDate = approveDate;
    }


}
