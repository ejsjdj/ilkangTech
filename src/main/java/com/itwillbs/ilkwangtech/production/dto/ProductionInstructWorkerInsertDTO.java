package com.itwillbs.ilkwangtech.production.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductionInstructWorkerInsertDTO {

    private Long operationId;
    private Long memberId;

    public ProductionInstructWorkerInsertDTO(Long operationId, Long memberId){
        this.operationId = operationId;
        this.memberId = memberId;
    }
}
