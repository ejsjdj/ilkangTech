package com.itwillbs.ilkwangtech.standard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProcessDTO {

    private String opertaionId;
    private String name;
    private String description;
    private String memberName;
    private String createdAt;

    @Builder
    public ProcessDTO(String opertaionId, String name, String description, String memberName, String createdAt){
        this.opertaionId = opertaionId;
        this.name = name;
        this.description = description;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }
}
