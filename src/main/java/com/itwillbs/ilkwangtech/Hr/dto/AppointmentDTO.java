package com.itwillbs.ilkwangtech.Hr.dto;

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
    private String approverId;
    private String preDept;
    private String currentDept;
    private String preRank;
    private String currentRank;
    private String workStatus;
    private LocalDate appointmentDate;

    @Builder
    public AppointmentDTO(Long appointmentId, String memberId, String approverId, String preDept, String currentDept, String currentRank, String preRank, String workStatus, LocalDate appointmentDate){
        super();
        this.appointmentId = appointmentId;
        this.memberId = memberId;
        this.approverId = approverId;
        this.preDept = preDept;
        this.currentDept = currentDept;
        this.preRank = preRank;
        this.currentRank = currentRank;
        this.workStatus = workStatus;
        this.appointmentDate = appointmentDate;
    }


}
