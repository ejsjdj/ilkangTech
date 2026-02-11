package com.itwillbs.ilkwangtech.sales.controller.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales/prices")
public class PriceApiController {

    // 1. 신규 단가 설정 (POST)
    // 협상을 통해 결정된 특정 품목/고객사의 새로운 단가를 등록합니다.
    @PostMapping
    public void createPrice() {
        // @RequestBody PriceRequestDTO
    }

    // 2. 현재 유효 단가 목록 조회 (GET)
    // 현재 날짜 기준으로 적용되고 있는 품목별 단가 리스트
    @GetMapping("/current")
    public void getCurrentPrices() {
    }

    // 3. 특정 제품/고객사의 단가 이력 조회 (GET)
    // 사용자가 요청한 "단가 수정 내용 리스트"를 보여주는 핵심 API
    @GetMapping("/history")
    public void getPriceHistory(@RequestParam String itemCode,
                                @RequestParam(required = false) Long customerId) {
    }

    // 4. 단가 정보 수정 (PUT)
    // 오입력 수정용. 금액이 변동된 것이라면 수정보다 '신규 등록'을 권장합니다.
    @PutMapping("/{id}")
    public void updatePrice(@PathVariable Long id) {
    }

    // 5. 단가 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public void deletePrice(@PathVariable Long id) {
    }
}