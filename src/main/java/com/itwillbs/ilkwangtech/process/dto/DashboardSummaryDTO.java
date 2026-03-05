package com.itwillbs.ilkwangtech.process.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DashboardSummaryDTO {
    private long todayInstructCount;    // 오늘 작업지시
    private long inProgressCount;      // 진행 중 작업
    private long qcFailedCount;       // QC 불합격
}
