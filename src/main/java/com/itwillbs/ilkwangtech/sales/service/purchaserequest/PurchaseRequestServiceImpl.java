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

    @Override
    @Transactional
    public List<PurchaseRequestHeaderDTO> getPruchaseRequest(){

        List<PurchaseRequestHeaderEntity> entity = purchaseRequestRepository.findAll();

        return entity.stream()
                .map(PurchaseRequestHeaderDTO::from)
                .toList();
    }

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
                                .itemId(line.getItemId())
                                .quantity(line.getQuantity())
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
