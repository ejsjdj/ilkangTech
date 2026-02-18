package com.itwillbs.ilkwangtech.sales.mapper;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseRequestMapper {

    // 신규 구매 요청 등록
    void createPurchase(PurchaseRequestDTO purchaseRequestDTO);

}
