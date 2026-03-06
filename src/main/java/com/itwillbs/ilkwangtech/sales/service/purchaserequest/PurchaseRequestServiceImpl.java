package com.itwillbs.ilkwangtech.sales.service.purchaserequest;

import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestHeaderDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseRequestLineDTO;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestHeaderEntity;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseRequestHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestServiceImpl implements PurchaseRequestService {

    private final PurchaseRequestHeaderRepository purchaseRequestRepository;

    // 구매요청 리스트 조회
    @Override
    @Transactional
    public List<PurchaseRequestHeaderDTO> getPruchaseRequest(){

        List<PurchaseRequestHeaderEntity> entity = purchaseRequestRepository.findAll();

        return entity.stream()
                .map(PurchaseRequestHeaderDTO::from)
                .toList();
    }

    // 구매요청 상세조회
    @Override
    @Transactional
    public PurchaseRequestDetailDTO getPurchaseReuqestDetail(Long id) {

        PurchaseRequestHeaderEntity header =
                purchaseRequestRepository.findDetailById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException("존재하지 않는 구매요청입니다."));


        List<PurchaseRequestLineDTO> lineDTOs =
                header.getLines().stream()
                        .map(line -> PurchaseRequestLineDTO.builder()
                                .id(line.getId())
                                .purchaseRequestLineId(line.getId())
                                .itemId(line.getItem().getItemId())
                                .itemName(line.getItem().getItemName())
                                .quantity(line.getQuantity())
                                .uom(line.getItem().getUom())
                                .price(line.getItem().getStandardPrice() * line.getQuantity())
                                .build()
                        )
                        .toList();

        return PurchaseRequestDetailDTO.builder()
                .id(header.getId())
                .purchaseRequestCode(header.getPurchaseRequestCode())
                .memberName(header.getMember().getName())
                .requestDate(header.getRequestDate())
                .dueDate(header.getDueDate())
                .deliveryLocate(header.getDeliveryLocate())
                .contractType(header.getContractType())
                .produceType(header.getProduceType())
                .lines(lineDTOs)
                .build();
    }
}
