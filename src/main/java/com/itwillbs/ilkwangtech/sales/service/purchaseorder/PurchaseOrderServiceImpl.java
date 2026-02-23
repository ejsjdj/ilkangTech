package com.itwillbs.ilkwangtech.sales.service.purchaseorder;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderInsertDTO;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    // 1. 발주 리스트 조회
    @Override
    @Transactional
    public Page<PurchaseOrderDTO> getPurchaseOrderList(Pageable pageable, String startDate, String endDate, String searchType, String keyword){

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Page<PurchaseOrderHeaderEntity> purchaseOrderEntities = purchaseOrderRepository.findByPurchaseOrder(pageable, start, end, searchType, keyword);

        return purchaseOrderEntities.map(purchaseOrderHeaderEntity -> PurchaseOrderDTO.builder().
                purchaseOrderCode(purchaseOrderHeaderEntity.getPurchaseOrderCode()).
                company(purchaseOrderHeaderEntity.getCompany()).
                name(purchaseOrderHeaderEntity.getName()).
                status(purchaseOrderHeaderEntity.getStatus()).
                orderDate(purchaseOrderHeaderEntity.getOrderDate()).
                build());
    }

    // 2. 발주 상세 조회
    @Override
    @Transactional
    public PurchaseOrderDetailDTO getPurchaseOrderDetail(Long purchaseOrderId){

        PurchaseOrderHeaderEntity detailEntity = purchaseOrderRepository.findByPurchaseIdDetail(purchaseOrderId);


        return null;
    }

    // 3. 신규 발주 등록
    @Override
    @Transactional
    public void savePurchaseOrder(List<PurchaseOrderInsertDTO> purchaseOrderInsertDTO){

    }
}
