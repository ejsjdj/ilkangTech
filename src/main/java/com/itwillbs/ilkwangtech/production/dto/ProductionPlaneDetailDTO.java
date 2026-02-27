package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProductionPlaneDetailDTO {

    private List<ProductionPlaneItemDTO> details; // 계획상세

    private String planeCode; // 계획코드

    private String routeCode;

    private LocalDateTime planeDate; // 계획일시

    private String memberName; // 등록자

    private Long item; // 폼목코드

    private Long totalQty; // 주문 총 수량

    private String status; // 생산 상태

    private String memo; // 메모

    public static ProductionPlaneDetailDTO fromDetail(ProductionPlaneEntity entity) {
        return ProductionPlaneDetailDTO.builder()
                .planeCode(entity.getPlaneCode())
                .routeCode(entity.getRoute().getRouteId())
                .planeDate(entity.getPlaneDate().toLocalDate().atStartOfDay())
                .memberName(entity.getMember().getName())
                .item(entity.getItem())
                .totalQty(entity.getTotalQty())
                .status(entity.getStatus())
                .memo(entity.getMemo())
                .details(entity.getDetails().stream()
                        .map(ProductionPlaneItemDTO::from)
                        .toList())
                .build();
    }

}
