package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionPlaneItemDTO {

    private Long id;
    private Long itemId;
    private String itemName;
    private Long orderId; // 수주 ID
    private Long productQty;
    private String memo;
    private String productionDetailDate;

    public static ProductionPlaneItemDTO from(ProductionPlaneDetailEntity entity) {
        return ProductionPlaneItemDTO.builder()
                .id(entity.getId())
                .itemId(entity.getItem().getItemId())
                .itemName(entity.getItem().getItemName())
                .orderId(entity.getOrderId())
                .productQty(entity.getProductQty())
                .memo(entity.getMemo())
                .productionDetailDate(String.valueOf(entity.getPlaneDetailDate()))
                .build();
    }
}