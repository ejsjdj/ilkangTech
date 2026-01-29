package com.itwillbs.ilkwangtech.Hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LeaveDTO {
    private long totalLeave;
    private long usedLeave;
    private long remainLeave;

    @Builder
    public LeaveDTO(long totalLeave, long usedLeave, long remainLeave){
        super();
        this.totalLeave = totalLeave;
        this.usedLeave = usedLeave;
        this.remainLeave = remainLeave;
    }
}
