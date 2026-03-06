package com.itwillbs.ilkwangtech.production.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessRegisterDTO {

    private Long sequence;
    private Long processId;
    private String processCode;
    private String processName;
    private Long outPutItemId;
    private Long productionQty;
    private String outputItemName;

    public ProcessRegisterDTO(
            Long sequence,
            Long processId,
            String processCode,
            String processName,
            Long outPutItemId,
            String outputItemName
    ) {
        this.sequence = sequence;
        this.processId = processId;
        this.processCode = processCode;
        this.processName = processName;
        this.outPutItemId = outPutItemId;
        this.outputItemName = outputItemName;
    }

}
