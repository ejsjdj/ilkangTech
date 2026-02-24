package com.itwillbs.ilkwangtech.sales.service.purchaseorder;


import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderInsertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PurchaseOrderService  {

    // 1. 발주 리스트 조회
    Page<PurchaseOrderDTO> getPurchaseOrderList(Pageable pageable, String startDate, String endDate, String searchType, String keyword);

    // 2. 발주 상세 조회
    PurchaseOrderDetailDTO getPurchaseOrderDetail(Long purchaseOrderId);

    // 3. 신규 발주 등록
    void savePurchaseOrder(PurchaseOrderInsertDTO purchaseOrderInsertDTO, Long userId);
}
