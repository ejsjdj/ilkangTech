package com.itwillbs.ilkwangtech.Hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorkStatusDTO {
    private String name;
    private String department;
    private String position;
    private LocalDateTime inTime; // 출근 시간
    private LocalDateTime goOutTime; // 퇴근 시간
    private LocalDateTime outTime;// 외근 시간
    private LocalDateTime returnTime; //복귀 시간
    private String status;

    @Builder
    public WorkStatusDTO(String name, String department, String position, LocalDateTime inTime, LocalDateTime goOutTime, LocalDateTime outTime, LocalDateTime returnTime, String status){
        super();
        this.name = name;
        this.department = department;
        this.position = position;
        this.inTime = inTime;
        this.goOutTime = goOutTime;
        this.outTime = outTime;
        this.returnTime = returnTime;
        this.status = status;
    }
}
