package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class ProductionWorkerDTO {

    // 등록용
    private Long instructId;
    private Long memberId;
    private Long processId;
    private Long newStatus;
    private String newLot;

    // 조회용
    private Long workerId;
    private String operationName;
    private String name;
    private String status;
    private String lot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long productionQty;
    private Long additionQty;
    private Long sequence;

    public static ProductionWorkerDTO fromList(ProductionWorkerEntity entity){
        return ProductionWorkerDTO.builder().
                workerId(entity.getId()).
                operationName(entity.getProcess().getName()).
                name(entity.getMember().getName()).
                status(entity.getStatus()).
                lot(entity.getLot()).
                startTime(entity.getStartTime()).
                endTime(entity.getEndTime()).
                productionQty(entity.getProductionQty()).
                additionQty(entity.getAdditionQty()).
                sequence(entity.getSequence()).
                build();

    }
}
