package com.itwillbs.ilkwangtech.Hr.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DraftApprovalLineDTO {
    private String draft_type; // 결재 양식
    private long sequence; // 순서 (프론트에서 순서 처리)
    private String name; // 사원명
    private String position; // 직급


    @Builder
    public DraftApprovalLineDTO(String draft_type, long sequence, String name, String position){
        super();
        this.draft_type = draft_type;
        this.sequence = sequence;
        this.name = name;
        this.position = position;
    }


}
