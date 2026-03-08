package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ProductionInstructDTO {

    // 작업지시번호
    private String instructCode;
    // 품목코드
    private Long item;
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
                instructCode(entity.getInstructCode()).
                item(entity.getItem()).
                instructQty(entity.getInstructQty()).
                startDate(String.valueOf(entity.getStartDate())).
                endDate(String.valueOf(entity.getEndDate())).
                status(entity.getStatus()).
                build();
    }


}
