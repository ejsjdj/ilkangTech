package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductionInstructDetailDTO {

    // 작업지시 번호
    private String instructCode;
    // 생산계획코드
    private String planeCode;
    // 품목명
    private String itemName;
    // 생산일자
    private String startDate;
    // 계획수량
    private Long instructQty;
    // 작업자
    private List<ProductionWorkerDTO> worker;
    // 상태
    private String status;

    public static ProductionInstructDetailDTO fromList(ProductionInstructEntity entity){
        return ProductionInstructDetailDTO.builder().
                instructCode(entity.getInstructCode()).
                planeCode(entity.getProductionId().getPlaneCode()).
                itemName(entity.getItem().getItemName()).
                startDate(String.valueOf(entity.getStartDate())).
                instructQty(entity.getInstructQty()).
                worker(entity.getWorkers().stream()
                        .map(ProductionWorkerDTO::fromList)
                        .toList()).
                build();
    }
}