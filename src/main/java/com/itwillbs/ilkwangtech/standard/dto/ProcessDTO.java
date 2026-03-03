package com.itwillbs.ilkwangtech.standard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProcessDTO {

    private Long id;
    private String operationId;
    private String name;
    private String description;
    private String memberName;
    private String createdAt;

    @Builder
    public ProcessDTO(Long id, String operationId, String name, String description, String memberName, String createdAt){
        this.id = id;
        this.operationId = operationId;
        this.name = name;
        this.description = description;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }
}
