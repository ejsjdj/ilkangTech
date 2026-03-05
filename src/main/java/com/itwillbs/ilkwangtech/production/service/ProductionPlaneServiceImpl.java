package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository;
import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.production.dto.*;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneDetailEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
import com.itwillbs.ilkwangtech.standard.entity.BomEntity;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import com.itwillbs.ilkwangtech.standard.repository.BomRepository;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

// 생산계획 서비스 구현체
@Service
@RequiredArgsConstructor
public class ProductionPlaneServiceImpl implements ProductionPlaneService {

    private final ProductionPlaneRepository productionPlaneRepository;
    private final MemberRepository memberRepository;
    private final ProcessRouteRepository processRouteRepository;
    private final ItemRepository itemRepository;
    private final BomRepository bomRepository;
    private final InventoryRepository inventoryRepository;

    // 1. 생산계획 목록 조회
    @Override
    @Transactional
    public Page<ProductionPlaneDTO> getProductionPlaneList(Pageable pageable, String keyword){

        Page<ProductionPlaneEntity> productionPlaneEntities = productionPlaneRepository.findByKeyword(pageable, keyword);

        return productionPlaneEntities
                .map(ProductionPlaneDTO::fromList);
    }

    // 2. 생산계획 상세 조회
    @Override
    @Transactional
    public Optional<ProductionPlaneDetailDTO> getProductionPlaneDetail(Long productionId){

        Optional<ProductionPlaneEntity> productionPlaneEntity = productionPlaneRepository.findDetailById(productionId);

        return productionPlaneEntity.map(ProductionPlaneDetailDTO::fromDetail);
    }

    // 3. 신규 생산계획 등록
    @Override
    @Transactional
    public void saveProductionPlane(ProductionPlaneInsertDTO productionPlaneInsertDTO, Long userId){
        // 1. 유저 정보 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

        ProcessRouteEntity routeCode = processRouteRepository.findById(productionPlaneInsertDTO.getRouteCode()).
                orElseThrow(() -> new IllegalArgumentException("라우트 코드가 존재하지 않습니다."));

        ItemEntity item = itemRepository.findById(productionPlaneInsertDTO.getItem())
                .orElseThrow(() -> new IllegalArgumentException("품목 정보가 존재하지 않습니다."));


        // 2. 헤더 엔티티 저장
        ProductionPlaneEntity header = ProductionPlaneEntity.saveHeader(
                productionPlaneInsertDTO.getPlaneCode(),
                productionPlaneInsertDTO.getPlaneDate(),
                member,
                item,
                productionPlaneInsertDTO.getTotalQty(),
                productionPlaneInsertDTO.getStatus(),
                productionPlaneInsertDTO.getMemo()
                );

        // 3. 상세 엔티티 저장
        for(ProductionPlaneItemDTO itemDTO : productionPlaneInsertDTO.getDetails()){

            ProductionPlaneDetailEntity detail = ProductionPlaneDetailEntity.create(
                    itemDTO.getOrderId(),
                    itemDTO.getProductQty(),
                    itemDTO.getMemo()
            );

            header.saveDetails(detail);

        }
    }

    @Override
    @Transactional
    // 4. 생산계획 및 작업지시 취소
    public void cancelProductionPlane(Long planeId){
        productionPlaneRepository.updatePlaneStatus(planeId);
        productionPlaneRepository.updateInstructStatusByPlaneId(planeId);
    }

    // 작업지시 리스트
    @Override
    @Transactional
    public List<ProcessRegisterDTO> getProcessInstructList(Long planeId){
        ProductionPlaneEntity plane = productionPlaneRepository.findById(planeId)
                .orElseThrow();

        return processRouteRepository.findProcessRegisterList(
                plane.getItem().getItemId()
        );
    }

    // 5. 생산계획 전체 조회
    public List<ProductionPlaneAllDTO> getProductionPlaneAll(){

        return productionPlaneRepository.findAllForSelect();

    }

    // 6. 재고검증
    @Override
    @Transactional
    public void checkStock(Long itemId, Long productionQty) {

        System.out.println("===== 재고 검증 시작 =====");
        System.out.println("생산 품목 ID: " + itemId);
        System.out.println("생산 수량: " + productionQty);


        // 1. BOM 전개 → 원자재 필요수량 Map
        Map<Long, Long> requiredRawMaterials =
                explodeBom(itemId, productionQty);

        System.out.println("BOM 전개 결과 (원자재 필요수량): " + requiredRawMaterials);

        // 2. 재고 비교
        List<String> shortageMessages = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : requiredRawMaterials.entrySet()) {

            Long rawItemId = entry.getKey();
            Long requiredQty = entry.getValue();

            Long stockQty =
                    inventoryRepository.getTotalQuantityByItemId(rawItemId);

            System.out.println("--------------------------------");
            System.out.println("원자재 ID: " + rawItemId);
            System.out.println("필요 수량: " + requiredQty);
            System.out.println("현재 재고: " + stockQty);

            if (stockQty < requiredQty) {

                Long shortage = requiredQty - stockQty;

                System.out.println("⚠ 재고 부족 발생 → 부족수량: " + shortage);


                shortageMessages.add(
                        "품목ID: " + rawItemId +
                                " 부족수량: " + shortage
                );

                // TODO: shortage 테이블 저장
            } else {
                System.out.println("재고 충분");
            }
        }

        // 3. 하나라도 부족하면 생산계획 등록 불가
        if (!shortageMessages.isEmpty()) {

            System.out.println("===== 재고 부족으로 생산 불가 =====");

            throw new IllegalStateException(
                    "재고 부족:\n" + String.join("\n", shortageMessages)
            );
        }
        System.out.println("===== 재고 검증 통과 =====");
    }

    private Map<Long, Long> explodeBom(Long itemId, Long qty) {

        System.out.println("BOM 전개 시작 → childItemId: " + itemId + ", qty: " + qty);

        Map<Long, Long> result = new HashMap<>();

        List<BomEntity> bomList =
                bomRepository.findByChildItem_ItemId(itemId);

        for (BomEntity bom : bomList) {

            ItemEntity material = bom.getParentItem();

            Long requiredQty = bom.getRequireQty() * qty;

            System.out.println(
                    "BOM 조회 → parent: " + material.getItemId()
                            + ", 타입: " + material.getItemType()
                            + ", 필요수량: " + bom.getRequireQty()
                            + ", 계산수량: " + requiredQty
            );

            // 원자재 → 종료
            if (material.getItemType() == ItemType.RAW) {

                System.out.println("RAW 발견 → ID: " + material.getItemId());

                result.merge(material.getItemId(), requiredQty, Long::sum);

            }
            // 반자재 / 재공품 / 완제품 → 재귀
            else if (material.getItemType() == ItemType.SEMI
                    || material.getItemType() == ItemType.WIP
                    || material.getItemType() == ItemType.FG) {

                System.out.println("재귀 BOM 전개 → ID: " + material.getItemId());

                Map<Long, Long> childMap =
                        explodeBom(material.getItemId(), requiredQty);

                childMap.forEach((k, v) -> result.merge(k, v, Long::sum));
            }
        }

        System.out.println("BOM 전개 결과 → " + result);

        return result;
    }


}
