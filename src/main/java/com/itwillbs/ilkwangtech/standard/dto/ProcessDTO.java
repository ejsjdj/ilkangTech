package com.itwillbs.ilkwangtech.standard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProcessDTO {

    private Long id;
    private String operationCode;
    private String name;
    private String description;
    private String memberName;
    private String createdAt;

    @Builder
    public ProcessDTO(Long id, String operationCode, String name, String description, String memberName, String createdAt){
        this.id = id;
        this.operationCode = operationCode;
        this.name = name;
        this.description = description;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }
}
