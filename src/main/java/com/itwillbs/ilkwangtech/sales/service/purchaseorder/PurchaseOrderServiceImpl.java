package com.itwillbs.ilkwangtech.sales.service.purchaseorder;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderInsertDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderLineDTO;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MemberRepository memberRepository;

    // 1. 발주 리스트 조회
    @Override
    @Transactional
    public Page<PurchaseOrderDTO> getPurchaseOrderList(Pageable pageable, String startDate, String endDate, String searchType, String keyword){

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Page<PurchaseOrderHeaderEntity> purchaseOrderEntities = purchaseOrderRepository.findByPurchaseOrder(pageable, start, end, searchType, keyword);

        return purchaseOrderEntities.
                map(purchaseOrderHeaderEntity -> PurchaseOrderDTO.
                        builder().
                        purchaseOrderCode(purchaseOrderHeaderEntity.getPurchaseOrderCode()).
                        company(purchaseOrderHeaderEntity.getCompany()).
                        name(purchaseOrderHeaderEntity.getName()).
                        status(purchaseOrderHeaderEntity.getStatus()).
                        orderDate(String.valueOf(purchaseOrderHeaderEntity.getOrderDate())).
                        build());
    }

    // 2. 발주 상세 조회
    @Override
    @Transactional
    public PurchaseOrderDetailDTO getPurchaseOrderDetail(Long purchaseOrderId){

        log.info("발주 상세 조회 service - 발주 ID: {}", purchaseOrderId);

        PurchaseOrderHeaderEntity header = purchaseOrderRepository.findByPurchaseIdDetail(purchaseOrderId);

        List<PurchaseOrderLineDTO> lineDto =
                header.getLines().stream()
                        .map(purchaseOrderEntity -> new PurchaseOrderLineDTO(
                                purchaseOrderEntity.getId(),
                                purchaseOrderEntity.getItem(),
                                purchaseOrderEntity.getQuantity(),
                                purchaseOrderEntity.getUnitPrice()
                        ))
                        .toList();

        PurchaseOrderDetailDTO detailDTO = PurchaseOrderDetailDTO.builder().
                purchaseOrderLineDto(lineDto).
                purchaseOrderCode(header.getPurchaseOrderCode()).
                company(header.getCompany()).
                companyManager(header.getCompanyManager()).
                phone(header.getPhone()).
                name(header.getName()).
                status(header.getStatus()).
                orderDate(String.valueOf(header.getOrderDate())).
                amount(header.getAmount()).
                build();

        log.info("발주 상세 조회 Service 결과 - 리스트: {}", detailDTO);

        return detailDTO;
    }

    // 3. 신규 발주 등록
    @Override
    @Transactional
    public void savePurchaseOrder(PurchaseOrderInsertDTO purchaseOrderInsertDTO, Long userId){

        // 1. 유저 정보 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

        // 2. 헤더 엔티티 저장
        PurchaseOrderHeaderEntity header = PurchaseOrderHeaderEntity.saveHeader(
                purchaseOrderInsertDTO.getPurchaseOrderCode(),
                purchaseOrderInsertDTO.getCompany(),
                purchaseOrderInsertDTO.getCompanyManager(),
                member.getPhoneNumber(),
                member.getName(),
                LocalDate.now()

        );

        // 3. 라인 엔티티 저장
        for(PurchaseOrderLineDTO lineDTO : purchaseOrderInsertDTO.getLines()){

            PurchaseOrderEntity line = PurchaseOrderEntity.create(
                    lineDTO.getItem(),
                    lineDTO.getQuantity(),
                    lineDTO.getUnitPrice());

            header.saveLine(line);
        }

    }
}
