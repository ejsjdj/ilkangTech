package com.itwillbs.ilkwangtech.hr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DraftDTO {
    private Long draft_id;
    private String draft_title;
    private String draft_content;
    private String draft_startDate;
    private String draft_endDate;
    private String draft_status;

    public DraftDTO(Long draftId,
                    String draftTitle,
                    String draftContent,
                    LocalDate draftStartDate,
                    LocalDate draftEndDate,
                    String draftStatus) {
        this.draft_id = draftId;
        this.draft_title = draftTitle;
        this.draft_content = draftContent;
        this.draft_startDate = draftStartDate != null ? draftStartDate.toString() : null;
        this.draft_endDate = draftEndDate != null ? draftEndDate.toString() : null;
        this.draft_status = draftStatus;
    }

}
