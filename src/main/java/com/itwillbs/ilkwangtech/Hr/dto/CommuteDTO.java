package com.itwillbs.ilkwangtech.Hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommuteDTO {
    private LocalDateTime inTime; // 출근 시간
    private LocalDateTime goOutTime; // 퇴근 시간
    private LocalDateTime outTime;// 외근 시간
    private LocalDateTime returnTime; //복귀 시간

    @Builder
    public CommuteDTO(LocalDateTime inTime, LocalDateTime goOutTime, LocalDateTime outTime, LocalDateTime returnTime){
        super();
        this.inTime = inTime;
        this.goOutTime = goOutTime;
        this.outTime = outTime;
        this.returnTime = returnTime;
  }
}
