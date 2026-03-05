package com.itwillbs.ilkwangtech.sales.service.purchasereturn;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseReturnDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseReturnInsertDTO;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseReturnEntity;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseRequestRepository;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseReturnServiceImpl implements PurchaseReturnService {

    private final PurchaseReturnRepository purchaseReturnRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;

    // 1. 반품 조회
    public Page<PurchaseReturnDTO> getReturnPurchase(Pageable pageable){
        Page<PurchaseReturnEntity> purchaseReturnEntities = purchaseReturnRepository.findAll(pageable);

        return purchaseReturnEntities.map(PurchaseReturnDTO::fromList);
    }


    // 2. 반품 등록
    @Override
    @Transactional
    public void returnPurchase(PurchaseReturnInsertDTO purchaseReturnInsertDTO, Long userId){

        Long purchaseRequestDetailId = purchaseReturnInsertDTO.getPurchaseRequestDetailId();
        System.out.println("반품 returnId : " + purchaseRequestDetailId);

        // 1. 유저 정보 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

        // 2. 구매요청 상세 엔티티 조회
        PurchaseRequestEntity request = purchaseRequestRepository.findById(purchaseReturnInsertDTO.getPurchaseRequestDetailId())
                .orElseThrow(() -> new IllegalArgumentException("구매요청 라인 정보가 없습니다."));

        // 3. 반품수량만큼 주문수량 차감
        Long requestQty = request.getQuantity();
        Long resultQty = requestQty - purchaseReturnInsertDTO.getReturnQty();
        request.setQuantity(resultQty);

        PurchaseReturnEntity entity = PurchaseReturnEntity.create(
                request,
                purchaseReturnInsertDTO.getReturnQty(),
                member,
                purchaseReturnInsertDTO.getMemo()
        );

        purchaseReturnRepository.save(entity);

    }

}
