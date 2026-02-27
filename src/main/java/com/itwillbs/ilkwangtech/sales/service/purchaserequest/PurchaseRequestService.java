package com.itwillbs.ilkwangtech.sales.service.purchaserequest;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestHeaderDTO;

import java.util.List;
import java.util.Optional;

public interface PurchaseRequestService {

    List<PurchaseRequestHeaderDTO> getPruchaseRequest();

    PurchaseRequestDetailDTO getPurchaseReuqestDetail(Long id);

}
