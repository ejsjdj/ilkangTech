package com.itwillbs.ilkwangtech.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LeaveDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private long totalLeave;
    private long usedLeave;
    private long remainLeave;

    @Builder
    public LeaveDTO(LocalDate startDate, LocalDate endDate, long totalLeave, long usedLeave, long remainLeave){
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalLeave = totalLeave;
        this.usedLeave = usedLeave;
        this.remainLeave = remainLeave;
    }
}
