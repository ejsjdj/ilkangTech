package com.itwillbs.ilkwangtech.sales.controller.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales/orders")
public class OrderApiController {

    // 1. 신규 수주 등록 (POST)
    // 수주 헤더(날짜, 고객사)와 상세 내역(품목, 수량)을 한 번에 받습니다.
    @PostMapping
    public void createOrder() {
        // @RequestBody OrderRequestDTO
    }

    // 2. 수주 목록 조회 (GET)
    // 기간, 고객사명, 진행 상태별 필터링이 필수입니다.
    @GetMapping
    public void getOrderList() {
    }

    // 3. 수주 상세 조회 (GET)
    @GetMapping("/{id}")
    public void getOrder(@PathVariable Long id) {
    }

    // 4. 수주 정보 수정 (PUT)
    // 수주가 이미 '생산 시작' 또는 '출고' 상태라면 수정을 제한하는 로직이 필요합니다.
    @PutMapping("/{id}")
    public void updateOrder(@PathVariable Long id) {
    }

    // 5. 수주 삭제 (DELETE)
    // 이력을 남기기 위해 실제 삭제보다는 '주문 취소' 상태로 변경하는 경우가 많습니다.
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
    }

    // 6. [추가 추천] 수주 상태 일괄 변경 (PATCH)
    // 수주 확정, 생산 요청 등 상태값만 바꿀 때 유용합니다.
    @PatchMapping("/{id}/status")
    public void changeOrderStatus(@PathVariable Long id, @RequestParam String status) {
    }

}