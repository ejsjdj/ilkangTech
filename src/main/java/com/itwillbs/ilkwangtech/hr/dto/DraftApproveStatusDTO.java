package com.itwillbs.ilkwangtech.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DraftApproveStatusDTO {

    private long draft_id;
    private String draft_title;
    private LocalDate draft_startTime;
    private LocalDate draft_endDate;
    private LocalDate draft_approvalDate;
    private String draft_status;

    @Builder
    public DraftApproveStatusDTO(long draft_id, String draft_title, LocalDate draft_startTime, LocalDate draft_endDate, LocalDate draft_approvalDate, String draft_status){
        super();
        this.draft_id = draft_id;
        this.draft_title = draft_title;
        this.draft_startTime = draft_startTime;
        this.draft_endDate = draft_endDate;
        this.draft_approvalDate = draft_approvalDate;
        this.draft_status = draft_status;
    }
}
