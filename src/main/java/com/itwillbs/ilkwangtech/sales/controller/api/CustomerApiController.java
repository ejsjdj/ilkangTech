package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import com.itwillbs.ilkwangtech.sales.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales/customers") // 공통 경로 설정
@RequiredArgsConstructor
public class CustomerApiController {

    private final CustomerService customerService;

    // 1. 고객사 추가 (POST)
    @PostMapping
    public void createCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.createCustomer(customerDTO);
    }

    // 2. 고객사 목록 조회 (GET)
    @GetMapping
    public void getCustomerList() {
        // 검색 조건 및 페이지네이션 처리
    }

    // 3. 고객사 상세정보 조회 (GET) - 특정 ID 사용
    @GetMapping("/{id}")
    public void getCustomer(@PathVariable Long id) {
    }

    // 4. 고객사 정보 수정 (PUT 또는 PATCH)
    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable Long id) {
        // 전체 수정은 PUT, 일부 수정은 PATCH 사용
    }

    // 5. 고객사 정보 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
    }
}