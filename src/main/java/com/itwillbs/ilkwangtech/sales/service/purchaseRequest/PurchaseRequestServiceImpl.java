package com.itwillbs.ilkwangtech.sales.service.purchaseRequest;


import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestDTO;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestServiceImpl implements PurchaseRequestService{

    private final PurchaseRequestRepository purchaseRequestRepository;

    // 신규 구매요청 등록
    @Override
    @Transactional
    public void createPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO){
        purchaseRequestRepository.createPurchase(purchaseRequestDTO);
    }

}
