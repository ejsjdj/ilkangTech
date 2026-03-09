package com.itwillbs.ilkwangtech.production.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductionInstructWorkerInsertDTO {

    private Long processId;
    private Long memberId;
    private Long productionQty;
    private Long additionQty;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long outputItemId;
    private Long sequence;

    public ProductionInstructWorkerInsertDTO(Long processId, Long memberId, Long productionQty, Long additionQty, LocalDateTime startTime, LocalDateTime endTime, Long outputItemId, Long sequence ){
        this.processId = processId;
        this.memberId = memberId;
        this.productionQty = productionQty;
        this.additionQty = additionQty;
        this.startTime = startTime;
        this.endTime = endTime;
        this.outputItemId = outputItemId;
        this.sequence = sequence;

    }
}
