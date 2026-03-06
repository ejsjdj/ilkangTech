package com.itwillbs.ilkwangtech.production.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductionInstructWorkerInsertDTO {

    private Long processId;
    private Long memberId;

    public ProductionInstructWorkerInsertDTO(Long processId, Long memberId){
        this.processId = processId;
        this.memberId = memberId;
    }
}
