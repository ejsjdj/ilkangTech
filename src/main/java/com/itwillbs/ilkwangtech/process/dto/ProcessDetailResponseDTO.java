package com.itwillbs.ilkwangtech.process.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProcessDetailResponseDTO {
    private String instructCode;
    private String itemName;
    private Integer instructQty;
    private List<StepDetail> steps;

    @Getter @Setter
    public static class StepDetail {
        private String processName;
        private String status;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer defectiveQty;
    }
}
