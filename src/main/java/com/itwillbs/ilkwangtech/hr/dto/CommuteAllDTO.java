package com.itwillbs.ilkwangtech.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommuteAllDTO {
    private String name;
    private String deptName;
    private LocalDate workDate;
    private LocalDateTime inTime; // 출근 시간
    private LocalDateTime goOutTime; // 퇴근 시간
    private LocalDateTime outTime;// 외근 시간
    private LocalDateTime returnTime; //복귀 시간

    @Builder
    public CommuteAllDTO(String name, String deptName, LocalDate workDate, LocalDateTime inTime, LocalDateTime goOutTime, LocalDateTime outTime, LocalDateTime returnTime){
        super();
        this.name = name;
        this.deptName = deptName;
        this.workDate = workDate;
        this.inTime = inTime;
        this.goOutTime = goOutTime;
        this.outTime = outTime;
        this.returnTime = returnTime;
    }

}
