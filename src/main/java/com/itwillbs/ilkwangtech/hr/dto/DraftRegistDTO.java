package com.itwillbs.ilkwangtech.hr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


// 결재 문서 등록 요청 DTO
@Getter
@Setter
@NoArgsConstructor
public class DraftRegistDTO {
    //======================사용자 입력=========================//
    private String draftType;
    private String draftTitle;
    private String draftContent;
    private String draftStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate draftStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate draftEndDate;
    private List<String> draftApprover;
    private String draftFile;



    @Builder
    public DraftRegistDTO(String draftType, String draftTitle, String draftContent, LocalDate draftStartDate, LocalDate draftEndDate, List<String> draftApprover, String draftFile, String draftStatus){
        super();
        this.draftType = draftType;
        this.draftTitle = draftTitle;
        this.draftContent = draftContent;
        this.draftStartDate = draftStartDate;
        this.draftEndDate = draftEndDate;
        this.draftApprover = draftApprover;
        this.draftFile = draftFile;
        this.draftStatus = draftStatus;
    }
}