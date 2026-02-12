package com.itwillbs.ilkwangtech.sales.controller.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales/performance")
public class PerformanceApiController {

    // 1. 매출 목록 조회 (GET)
    // 날짜 범위, 고객사별, 품목별 필터링
    @GetMapping
    public void getPerformanceList() {
    }

    // 2. 월별/분기별 매출 통계 조회 (GET)
    // 차트(Chart.js 등) 시각화를 위한 집계 데이터
    @GetMapping("/statistics/monthly")
    public void getMonthlyStatistics() {
    }

    // 3. 고객사별 매출 순위/비중 조회 (GET)
    @GetMapping("/statistics/Company")
    public void getPerformanceByCustomer() {
    }

    // 4. 매출 상세 내역 (GET)
    // 특정 매출 건에 대한 상세 정보(연결된 인보이스 및 품목 내역)
    @GetMapping("/{id}")
    public void getPerformanceDetail(@PathVariable Long id) {
    }

    // ※ 주의: 매출은 인보이스에서 파생되므로 직접적인 '등록/수정/삭제'는
    // 기초 데이터(인보이스/수주)에서 처리하고 여기서는 '조정' 개념으로 접근합니다.

    // 5. 매출 데이터 조정 (PUT)
    // 세무 조정이나 반품 처리 등으로 인한 금액 수정 시 사용
    @PutMapping("/{id}/adjust")
    public void adjustPerformance(@PathVariable Long id) {
    }
}