package com.itwillbs.ilkwangtech.sales.controller;

import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import com.itwillbs.ilkwangtech.sales.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sales/customer")
@RequiredArgsConstructor
public class CustomerMasterController {

    private final CustomerService customerService;

    // 고객사 관리 메인 페이지
    @GetMapping("/list")
    public String customerList(Model model) {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        
//        // 통계 데이터 계산
//        long totalCustomers = customers.size();
//        long activeCustomers = customers.stream().filter(c -> "ACTIVE".equals(c.getStatus())).count();
//        long newCustomers = customers.stream().filter(c -> {
//            return c.getCreatedAt() != null &&
//                   c.getCreatedAt().getMonthValue() == java.time.LocalDateTime.now().getMonthValue();
//        }).count();
        
//        model.addAttribute("customers", customers);
//        model.addAttribute("totalCustomers", totalCustomers);
//        model.addAttribute("activeCustomers", activeCustomers);
//        model.addAttribute("newCustomers", newCustomers);
//        model.addAttribute("totalOrders", 0); // TODO: 실제 주문 데이터로 계산
        
        return "sales/customer/customer-list";
    }

    // 고객사 상세 페이지
    @GetMapping("/{id}")
    public String customerDetail(@PathVariable Long id, Model model) {
        CustomerDTO customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "sales/customer/customer-detail";
    }

    // ===== REST API 엔드포인트 =====

    // 모든 고객사 조회
    @GetMapping("/api/list")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // 고객사 상세조회
    @GetMapping("/api/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // 신규 고객사 생성
    @PostMapping("/api")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    // 고객사 정보 수정
    @PutMapping("/api/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    // 고객사 삭제
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    // 고객사 검색
    @PostMapping("/api/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestBody Map<String, String> searchParams) {
        String name = searchParams.get("name");
        String businessNo = searchParams.get("businessNo");
        String manager = searchParams.get("manager");
        
        List<CustomerDTO> customers = customerService.searchCustomers(name, businessNo, manager);
        return ResponseEntity.ok(customers);
    }

}
