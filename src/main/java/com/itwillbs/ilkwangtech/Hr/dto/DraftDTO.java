package com.itwillbs.ilkwangtech.Hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DraftDTO {
    private long draft_id;
    private String draft_title;
    private String draft_content;
    private LocalDate draft_startTime;
    private LocalDate draft_endDate;
    private LocalDate draft_approvalDate;
    private String draft_status;

    @Builder
    public DraftDTO(long draft_id, String draft_title, String draft_content, LocalDate draft_startTime, LocalDate draft_endDate, LocalDate draft_approvalDate, String draft_status){
        super();
        this.draft_id = draft_id;
        this.draft_title = draft_title;
        this.draft_content = draft_content;
        this.draft_startTime = draft_startTime;
        this.draft_endDate = draft_endDate;
        this.draft_approvalDate = draft_approvalDate;
        this.draft_status = draft_status;
    }


}
