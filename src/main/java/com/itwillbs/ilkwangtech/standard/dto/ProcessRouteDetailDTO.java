package com.itwillbs.ilkwangtech.standard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcessRouteDetailDTO {

    private Long id; // 라우트 단계 id
    private Long operationId; // 공정 FK
    private String operationCode; // 공정 코드
    private String name; // 공정명
    private String description; // 공정 설명
    private Long sequence;
    private String note; // 비고

    @Builder
    public ProcessRouteDetailDTO(Long id, Long operationId, String operationCode, String name, String description, Long sequence,String note){
        this.id = id;
        this.operationId = operationId;
        this.operationCode = operationCode;
        this.name = name;
        this.description = description;
        this.sequence = sequence;
        this.note = note;
    }

}
