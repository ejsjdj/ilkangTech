package com.itwillbs.ilkwangtech.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 근무현황 반환
@Getter
@Setter
@NoArgsConstructor
public class WorkStatusDTO {
    private String name;
    private String department;
    private String position;
    private String status;

    @Builder
    public WorkStatusDTO(String name, String department, String position, String status){
        super();
        this.name = name;
        this.department = department;
        this.position = position;
        this.status = status;
    }
}
