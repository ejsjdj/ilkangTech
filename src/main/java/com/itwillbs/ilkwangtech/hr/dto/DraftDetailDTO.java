package com.itwillbs.ilkwangtech.hr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DraftDetailDTO {
    private String detailTitle;
    private String detailContent;
    private String detailFile;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate detailStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate detailEndDate;

    @Builder
    public DraftDetailDTO(String detailTitle, String detailContent, String detailFile, LocalDate detailStartDate, LocalDate detailEndDate){
        this.detailTitle = detailTitle;
        this.detailContent = detailContent;
        this.detailFile = detailFile;
        this.detailStartDate = detailStartDate;
        this.detailEndDate = detailEndDate;
    }

}
