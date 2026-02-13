package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestDTO;
import com.itwillbs.ilkwangtech.sales.mapper.PurchaseRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PurchaseRequestRepository {

    private final PurchaseRequestMapper purchaseRequestMapper;

    // 신규 구매요청 등록
    public void createPurchase(PurchaseRequestDTO purchaseRequestDTO) {
        purchaseRequestMapper.createPurchase(purchaseRequestDTO);
    }
}
