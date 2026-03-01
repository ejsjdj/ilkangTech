package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 생산계획 목록 DTO
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductionPlaneDTO {

    private Long id; // 기본키

    private String planeCode; // 1. 계획코드

    private LocalDate planeDate; // 2. 계획일시

    private String memberName; // 3. 등록자

    private String itemName; // 4. 폼목명

    private Long totalQty; // 5. 총 생산 수량

    private String status; // 6. 생산 상태

    public static ProductionPlaneDTO fromList(ProductionPlaneEntity entity){
        return ProductionPlaneDTO.builder().
                id(entity.getId()).
                planeCode(entity.getPlaneCode()).
                planeDate(LocalDate.from(entity.getPlaneDate())).
                memberName(entity.getMember().getName()).
                itemName(entity.getItem().getItemName()).
                totalQty(entity.getTotalQty()).
                status(entity.getStatus()).
                build();
    }
}
