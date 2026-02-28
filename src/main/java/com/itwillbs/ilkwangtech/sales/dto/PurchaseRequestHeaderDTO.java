package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestHeaderEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
public class PurchaseRequestHeaderDTO {

    private Long id; // 기본키
    private String purchaseRequestCode; // 구매코드

    public static PurchaseRequestHeaderDTO from(PurchaseRequestHeaderEntity entity){
        return PurchaseRequestHeaderDTO.builder().
                id(entity.getId()).
                purchaseRequestCode(entity.getPurchaseRequestCode()).
                build();
    }

}