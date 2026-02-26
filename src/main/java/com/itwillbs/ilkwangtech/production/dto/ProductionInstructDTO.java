package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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
    private LocalDate startDate;
    // 종료시간
    private LocalDate endDate;
    // 상태
    private String status;

//    public static ProductionInstructDTO fromList(ProductionInstructEntity entity){
//        return ProductionInstructDTO.builder().
//                instructCode(entity.getPlaneCode()).
//                item(LocalDate.from(entity.getPlaneDate())).
//                instructQty(entity.getMember().getName()).
//                startDate(entity.getItem()).
//                endDate(entity.getStatus()).
//                status(entity.getMemo()).
//                build();
//    }


}
