package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductionWorkderDTO {

    private String operationName;
    private String name;

    public static ProductionWorkderDTO fromList(ProductionWorkerEntity entity){
        return ProductionWorkderDTO.builder().
                operationName(entity.getProcess().getName()).
                name(entity.getMember().getName()).
                build();

    }
}
