package com.itwillbs.ilkwangtech.Hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DraftDetailDTO {
    private String title;
    private String content;
    private String file;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public DraftDetailDTO(String title, String content, String file, LocalDate startDate, LocalDate endDate){
        this.title = title;
        this.content = content;
        this.file = file;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
