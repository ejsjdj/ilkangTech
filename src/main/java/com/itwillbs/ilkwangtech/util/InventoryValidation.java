package com.itwillbs.ilkwangtech.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 재고 확인 컴포넌트
@Component
@RequiredArgsConstructor
public class InventoryValidation {

    // private final InventoryRepository inventoryRepository;

    // 1. 완제품 재고 확인
    public boolean checkFinishedProduct(){

        /* *
         * 1. 완제품 재고 확인
         *
         * (완제품A 가용량) = [완재품A 재고] + [입고 예정 수량] - [출하 예정 수량]
         *
         * */
        return true;
    }

    // 2. 원자재 재고 확인
    public long calculateAvailableMaterial() {

        /* *
         *
         * (원자재A 가용량) = [원자재A 재고] - [원자재A 안전재고] - [예약 재고A] + [입고 예정 재고A]
         *
         * */
        return 1;
    }

    // 3. 생산 가능 수량 확인
    public long getPossibleProductionQty() {

        /* *
         *
         * 완제품A 생산 가능 수량 = Min(원자재A 가용량/1, 원자재B 가용량/1, ...)
         *
         * */
        return 1;
    }

}
