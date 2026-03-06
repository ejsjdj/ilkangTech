package com.itwillbs.ilkwangtech.standard.dto;

import com.itwillbs.ilkwangtech.standard.constant.ProcessStatus;
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
    private ProcessStatus status;

    @Builder
    public ProcessDTO(Long id, String operationCode, String name, String description, String memberName, String createdAt, ProcessStatus status){
        this.id = id;
        this.operationCode = operationCode;
        this.name = name;
        this.description = description;
        this.memberName = memberName;
        this.createdAt = createdAt;
        this.status = status;
    }
}
