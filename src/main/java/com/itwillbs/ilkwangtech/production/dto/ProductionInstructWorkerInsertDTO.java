package com.itwillbs.ilkwangtech.production.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductionInstructWorkerInsertDTO {

    private Long processId;
    private Long memberId;
    private Long productionQty;
    private Long additionQty;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long outputItemId;

    public ProductionInstructWorkerInsertDTO(Long processId, Long memberId, Long productionQty, Long additionQty, LocalTime startTime, LocalTime endTime, Long outputItemId ){
        this.processId = processId;
        this.memberId = memberId;
        this.productionQty = productionQty;
        this.additionQty = additionQty;
        this.startTime = startTime;
        this.endTime = endTime;
        this.outputItemId = outputItemId;
        
    }
}
