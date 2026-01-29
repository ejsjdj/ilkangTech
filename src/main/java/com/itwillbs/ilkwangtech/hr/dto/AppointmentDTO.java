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
    private Long memberId;
    private Long approverId;
    private int preDept;
    private int currentDept;
    private int preRank;
    private int currentRank;
    private String workStatus;
    private LocalDate appointmentDate;

    @Builder
    public AppointmentDTO(Long appointmentId, Long memberId, Long approverId, int preDept, int currentDept, int currentRank, int preRank, String workStatus, LocalDate appointmentDate){
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
