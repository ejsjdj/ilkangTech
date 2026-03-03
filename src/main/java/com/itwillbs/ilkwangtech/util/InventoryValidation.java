package com.itwillbs.ilkwangtech.util;

import com.itwillbs.ilkwangtech.production.dto.NetRequirementDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductRequirementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// 재고 확인 컴포넌트
@Component
@RequiredArgsConstructor
public class InventoryValidation {

    // private final InventoryRepository inventoryRepository;

    // 1. 완제품 재고 확인
    public ProductRequirementDTO checkFinishedProduct(){

        /* *
         * 1. 완제품 재고 확인
         *
         * (완제품A 가용량) = [완재품A 재고] + [입고 예정 수량] - [출하 예정 수량]
         * (완제품A 필요 수량) = [생산해야하는 완제품A 수량] - [완재품A 가용량]
         * */
        // Q-Class 인스턴스
//        QItemEntity item = QItemEntity.itemEntity;
//        QStockEntity stock = QStockEntity.stockEntity;
//
//        return queryFactory
//                .select(Projections.constructor(ProductRequirementDTO.class,
//                        item.id,
//                        item.name,
//                        stock.stockQty.coalesce(0L),
//                        stock.incomingQty.coalesce(0L),
//                        stock.outgoingQty.coalesce(0L),
//                        // (가용량) = [재고] + [입고 예정] - [출하 예정]
//                        stock.stockQty.add(stock.incomingQty).subtract(stock.outgoingQty)
//                ))
//                .from(item)
//                .leftJoin(stock).on(item.id.eq(stock.itemId))
//                .where(item.id.eq(targetItemId))
//                .fetchOne();
        return null;
    }

    // 2. 원자재 재고 확인
    public List<NetRequirementDTO> calculateAvailableMaterial() {

        /* *
         *
         * (원자재A 가용량) = [원자재A 재고] - [원자재A 안전재고] - [예약 재고A] + [입고 예정 재고A]
         * (원자재A 필요수량) = [완제품 수량 * BOM 소요량] - [원자재A 가용량]
         * */

//        return queryFactory
//                .select(Projections.constructor(NetRequirementDTO.class,
//                        bom.parentItemId, // 원자재 ID (데이터가 뒤집혀 있으므로 parent가 원자재)
//
//                        // 1. 총 소요량 (BOM 소요량 * 완제품 주문 수량)
//                        bom.requireQty.multiply(orderQty).as("grossRequirement"),
//
//                        // 2. 가용량 계산 (재고 - 안전 - 예약 + 입고예정)
//                        stock.stockQty
//                                .subtract(stock.safetyQty)
//                                .subtract(stock.reservedQty)
//                                .add(stock.incomingQty).as("availableQty")
//                ))
//                .from(bom)
//                // 재고 테이블과 조인 (원자재 ID 기준)
//                .leftJoin(stock).on(bom.parentItemId.eq(stock.itemId))
//                // 데이터가 뒤집혀 있으므로 child_item_id에서 완제품(9번)을 찾음
//                .where(bom.childItemId.eq(targetParentId))
//                .fetch();

        return null;
    }

    // 3. 생산 가능 수량 확인
    public long getPossibleProductionQty() {

        /* *
         *
         * 완제품A 생산 가능 수량 = Min(원자재A 가용량/1, 원자재B 가용량/1, ...)
         *
         * */
//        QBomEntity bom = QBomEntity.bomEntity;
//        QStockEntity stock = QStockEntity.stockEntity;
//
//        return queryFactory
//                .select(Projections.fields(ComponentAvailabilityDTO.class,
//                        bom.parentItemId.as("materialId"), // 데이터 뒤집힘 반영: parent가 원자재
//                        bom.requireQty,
//                        // 가용량 계산: 재고 + 입고예정 - 안전재고 - 예약재고
//                        stock.stockQty.add(stock.incomingQty)
//                                .subtract(stock.safetyQty)
//                                .subtract(stock.reservedQty).as("availableQty")
//                ))
//                .from(bom)
//                .leftJoin(stock).on(bom.parentItemId.eq(stock.itemId))
//                .where(bom.childItemId.eq(targetParentId)) // 완제품 ID 조건
//                .fetch();

        return 1;
    }

}
