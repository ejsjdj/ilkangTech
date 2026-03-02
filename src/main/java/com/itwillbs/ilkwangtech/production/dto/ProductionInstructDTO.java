package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ProductionInstructDTO {
    // 기본키
    private Long id;
    //생산계획코드
    private String planeCode;
    // 작업지시코드
    private String instructCode;
    // 품목명
    private String item;
    // 계획수량
    private Long instructQty;
    // 시작시간
    private String startDate;
    // 종료시간
    private String endDate;
    // 상태
    private String status;

    public static ProductionInstructDTO fromList(ProductionInstructEntity entity){
        return ProductionInstructDTO.builder().
                id(entity.getId()).
                planeCode(entity.getProductionId().getPlaneCode()).
                instructCode(entity.getInstructCode()).
                item(entity.getItem().getItemName()).
                instructQty(entity.getInstructQty()).
                startDate(String.valueOf(entity.getStartDate())).
                endDate(String.valueOf(entity.getEndDate())).
                status(entity.getStatus()).
                build();
    }


}
