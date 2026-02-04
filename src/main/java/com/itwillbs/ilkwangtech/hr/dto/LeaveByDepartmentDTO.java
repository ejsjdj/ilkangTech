package com.itwillbs.ilkwangtech.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LeaveByDepartmentDTO {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long totalDate;

    @Builder
    public LeaveByDepartmentDTO(String name, LocalDate startDate, LocalDate endDate, String status, Long totalDate){
        super();
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalDate = totalDate;
    }

}
