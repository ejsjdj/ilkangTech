package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneDetailEntity;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ProductionPlaneItemDTO {

    private Long orderId;
    private Long productQty;
    private String memo;

    public static ProductionPlaneItemDTO from(ProductionPlaneDetailEntity entity) {
        return ProductionPlaneItemDTO.builder()
                .orderId(entity.getOrderId())
                .productQty(entity.getProductQty())
                .memo(entity.getMemo())
                .build();
    }
}