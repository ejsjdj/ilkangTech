package com.itwillbs.ilkwangtech.sales.service.purchaseorder;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderInsertDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderLineDTO;
import com.itwillbs.ilkwangtech.sales.entity.CompanyEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity;
import com.itwillbs.ilkwangtech.sales.repository.CompanyInterfaceRepository;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderHeaderRepository;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderRepository;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
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

    private final PurchaseOrderHeaderRepository purchaseOrderHeaderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CompanyInterfaceRepository companyRepository;


    // 1. 발주 리스트 조회
    @Override
    @Transactional
    public Page<PurchaseOrderDTO> getPurchaseOrderList(Pageable pageable, LocalDate startDate, LocalDate endDate, String searchType, String keyword){

        Page<PurchaseOrderHeaderEntity> purchaseOrderEntities = purchaseOrderHeaderRepository.findByPurchaseOrder(pageable, startDate, endDate, searchType, keyword);

        return purchaseOrderEntities.
                map(purchaseOrderHeaderEntity -> PurchaseOrderDTO.
                        builder().
                        id(purchaseOrderHeaderEntity.getId()).
                        purchaseOrderCode(purchaseOrderHeaderEntity.getPurchaseOrderCode()).
                        company(purchaseOrderHeaderEntity.getCompany().getCompanyName()).
                        companyManager(purchaseOrderHeaderEntity.getCompany().getCeoName()).
                        phone(purchaseOrderHeaderEntity.getCompany().getTelNo()).
                        name(purchaseOrderHeaderEntity.getMember().getName()).
                        status(purchaseOrderHeaderEntity.getStatus()).
                        orderDate(String.valueOf(purchaseOrderHeaderEntity.getOrderDate())).
                        amount(purchaseOrderHeaderEntity.getAmount()).
                        build());
    }

    // 2. 발주 상세 조회
    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderDetailDTO getPurchaseOrderDetail(Long purchaseOrderId){

        log.info("발주 상세 조회 service - 발주 ID: {}", purchaseOrderId);

        // 헤더 조회
        PurchaseOrderHeaderEntity header =
                purchaseOrderHeaderRepository.findById(purchaseOrderId)
                        .orElseThrow(() -> new IllegalArgumentException("발주 정보 없음"));

        // 라인 조회
        List<PurchaseOrderEntity> lines =
                purchaseOrderRepository.findByHeaderId(purchaseOrderId);

        List<PurchaseOrderLineDTO> lineDto =
                lines.stream()
                        .map(line -> new PurchaseOrderLineDTO(
                                line.getId(),
                                line.getItem().getItemId(),
                                line.getItem().getItemName(),
                                line.getQuantity(),
                                line.getUnitPrice()
                        ))
                        .toList();

        // 상세 DTO 생성
        PurchaseOrderDetailDTO detailDTO = PurchaseOrderDetailDTO.builder()
                .purchaseOrderLineDto(lineDto)
                .purchaseOrderCode(header.getPurchaseOrderCode())
                .company(header.getCompany().getCompanyName())
                .companyManager(header.getCompany().getCeoName())
                .phone(header.getCompany().getTelNo())
                .name(header.getMember().getName())
                .status(header.getStatus())
                .orderDate(String.valueOf(header.getOrderDate()))
                .amount(header.getAmount())
                .build();

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

        CompanyEntity company = companyRepository.findById(purchaseOrderInsertDTO.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("거래처 항목이 없습니다."));

        // 2. 헤더 엔티티 저장
        PurchaseOrderHeaderEntity header = PurchaseOrderHeaderEntity.saveHeader(
                purchaseOrderInsertDTO.getPurchaseOrderCode(),
                company,
                member,
                LocalDate.now()
        );

        purchaseOrderHeaderRepository.save(header);

        Long totalAmount = 0L;

        // 3. 라인 엔티티 저장
        for (PurchaseOrderLineDTO lineDTO : purchaseOrderInsertDTO.getLines()) {

            ItemEntity item = itemRepository.findById(lineDTO.getItem())
                    .orElseThrow(() -> new IllegalArgumentException("품목정보가 없습니다."));

            totalAmount += lineDTO.getTotalPrice();

            PurchaseOrderEntity line = PurchaseOrderEntity.create(
                    item,
                    lineDTO.getQuantity(),
                    lineDTO.getTotalPrice()
            );
            line.setHeader(header);

            purchaseOrderRepository.save(line);
        }

        header.setAmount(totalAmount);
    }

    @Override
    @Transactional
    // 5. 검수 완료 처리
    public void completeQcPurchase(Long purchaseOrderId){

        purchaseOrderHeaderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new IllegalArgumentException("발주 정보가 없습니다."));

        purchaseOrderHeaderRepository.updateByPurchaseId(purchaseOrderId);
    }
}
