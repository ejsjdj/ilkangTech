package com.itwillbs.ilkwangtech.sales.controller.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales/invoices")
public class InvoiceApiController {

    // 1. 인보이스 생성/발행 (POST)
    // 보통 수주번호(orderId)를 기반으로 데이터를 생성하는 경우가 많습니다.
    @PostMapping
    public void createInvoice() {
        // @RequestBody InvoiceRequestDTO
    }

    // 2. 인보이스 목록 조회 (GET)
    // 기간별 조회, 고객사별 조회, 미결제 인보이스 조회 등 필터가 중요합니다.
    @GetMapping
    public void getInvoiceList() {
    }

    // 3. 인보이스 상세 조회 (GET)
    @GetMapping("/{id}")
    public void getInvoice(@PathVariable Long id) {
    }

    // 4. 인보이스 정보 수정 (PUT)
    // 단, 이미 확정되어 국세청 전송 등이 끝난 인보이스는 수정이 불가능하도록 로직 처리가 필요합니다.
    @PutMapping("/{id}")
    public void updateInvoice(@PathVariable Long id) {
    }

    // 5. 인보이스 삭제/취소 (DELETE)
    // 인보이스는 세무 기록과 연결되므로 단순 삭제보다는 '취소(Cancel)' 상태로 변경하는 경우가 많습니다.
    @DeleteMapping("/{id}")
    public void cancelInvoice(@PathVariable Long id) {
    }

    // 6. [추가 추천] 인보이스 상태 변경 (PATCH)
    // '결제 완료' 처리 등을 위한 부분 업데이트
    @PatchMapping("/{id}/status")
    public void updateInvoiceStatus(@PathVariable Long id, @RequestParam String status) {
    }
}