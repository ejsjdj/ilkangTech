package com.itwillbs.ilkwangtech.Hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


// 결재 문서 등록 요청 DTO
@Getter
@Setter
@NoArgsConstructor
public class DraftRegistDTO {
    private String draft_type;
    private String draft_title;
    private String draft_content;
    private LocalDate draft_startDate;
    private LocalDate draft_endDate;
    private String draft_file;
    private long draft_id;
    private String draft_approver;
    private long draft_sequence;
    private String draft_status;

    @Builder
    public DraftRegistDTO(String draft_type, String draft_title, String draft_content, LocalDate draft_startDate, LocalDate draft_endDate, String draft_file, long draft_id, String draft_approver, long draft_sequence, String draft_status){
        super();
        this.draft_type = draft_type;
        this.draft_title = draft_title;
        this.draft_content = draft_content;
        this.draft_startDate = draft_startDate;
        this.draft_endDate = draft_endDate;
        this.draft_file = draft_file;

        this.draft_id = draft_id;
        this.draft_approver = draft_approver;
        this.draft_sequence = draft_sequence;
        this.draft_status = draft_status;
    }

}
