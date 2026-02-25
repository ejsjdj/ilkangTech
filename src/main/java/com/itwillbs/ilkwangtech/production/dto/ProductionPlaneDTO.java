package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 생산계획 목록 DTO
@Getter
@Setter
@Builder
public class ProductionPlaneDTO {

    private String planeCode; // 계획코드

    private String routeCode; // 라우트코드

    private LocalDate planeDate; // 계획일시

    private String memberName; // 등록자

    private Long item; // 폼목코드

    private Long totalQty; // 주문 총 수량

    private String status; // 생산 상태

    private String memo; // 메모

    public static ProductionPlaneDTO fromList(ProductionPlaneEntity entity){
        return ProductionPlaneDTO.builder().
                planeCode(entity.getPlaneCode()).
                planeDate(LocalDate.from(entity.getPlaneDate())).
                memberName(entity.getMember().getName()).
                item(entity.getItem()).
                status(entity.getStatus()).
                memo(entity.getMemo()).
                build();
    }
}
