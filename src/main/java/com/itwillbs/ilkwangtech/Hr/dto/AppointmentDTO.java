package com.itwillbs.ilkwangtech.Hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentDTO {

    private Long appointmentId;
    private Long memberId;
    private Long approverId;
    private String previousDeptId;
    private String previoutPositionId;
    private String workStatus;
    private LocalDate appointmentDate;

    @Builder
    public AppointmentDTO(Long appointmentId, Long memberId, Long approverId, String previousDeptId, String previoutPositionId, String workStatus, LocalDate appointmentDate){
        super();
        this.appointmentId = appointmentId;
        this.memberId = memberId;
        this.approverId = approverId;
        this.previousDeptId = previousDeptId;
        this.previoutPositionId = previoutPositionId;
        this.workStatus = workStatus;
        this.appointmentDate = appointmentDate;
    }


}
