package com.itwillbs.ilkwangtech.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CommuteDTO {
    private LocalDate workDate;
    private String inTime; // 출근 시간
    private String goOutTime; // 퇴근 시간
    private String outTime;// 외근 시간
    private String returnTime; //복귀 시간

    @Builder
    public CommuteDTO(LocalDate workDate, LocalDateTime inTime, LocalDateTime goOutTime, LocalDateTime outTime, LocalDateTime returnTime){
        super();
        this.workDate = workDate;
        this.inTime = inTime != null ? inTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "";
        this.goOutTime = goOutTime != null ? goOutTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "";
        this.outTime = outTime != null ? outTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "";
        this.returnTime = returnTime != null ? returnTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "";
  }
}
