package com.itwillbs.ilkwangtech.sales.service.purchasereturn;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseReturnDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseReturnInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseReturnService {

    // 반품목록 조회
    Page<PurchaseReturnDTO> getReturnPurchase(Pageable pageable);


    // 반품 등록
    void returnPurchase(PurchaseReturnInsertDTO purchaseReturnInsertDTO, Long userId);
}
