package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductionWorkerDTO {


    private String operationName;
    private String name;

    public static ProductionWorkerDTO fromList(ProductionWorkerEntity entity){
        return ProductionWorkerDTO.builder().
                operationName(entity.getProcess().getName()).
                name(entity.getMember().getName()).
                build();

    }
}
