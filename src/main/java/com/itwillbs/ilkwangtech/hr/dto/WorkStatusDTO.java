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
    private Long memberId;
    private String name;
    private String department;
    private String position;
    private String status;

    @Builder
    public WorkStatusDTO(Long memberId, String name, String department, String position, String status){
        super();
        this.memberId = memberId;
        this.name = name;
        this.department = department;
        this.position = position;
        this.status = status;
    }
}
