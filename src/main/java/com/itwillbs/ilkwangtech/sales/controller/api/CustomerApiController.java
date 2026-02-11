package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.sales.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import com.itwillbs.ilkwangtech.sales.dto.PageResponseDTO;
import com.itwillbs.ilkwangtech.sales.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/customers")
@RequiredArgsConstructor
public class CustomerApiController {

    private final CustomerService customerService;

    // 1. 고객사 등록
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<Void>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        System.out.println("customerDTO = " + customerDTO);
        customerService.createCustomer(customerDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("고객사 등록에 성공했습니다."));
    }

    // 2. 고객사 목록 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<CustomerDTO>>> getCustomerList(Pageable pageable) {
        List<CustomerDTO> list = customerService.getCustomerList(pageable);
        long total = customerService.getTotalCount();

        PageResponseDTO<CustomerDTO> pageData = new PageResponseDTO<>(
                list, total, (int) Math.ceil((double) total / pageable.getPageSize())
        );

        return ResponseEntity.ok(ApiResponseDTO.success(pageData));
    }

    // 3. 고객사 상세 조회
    @GetMapping("/{customerId}") // 변수명 통일
    public ResponseEntity<ApiResponseDTO<CustomerDTO>> getCustomer(@PathVariable Long customerId) {
        CustomerDTO customerDTO = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(ApiResponseDTO.success(customerDTO));
    }

    // 4. 고객사 정보 수정
    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponseDTO<Void>> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerDTO customerDTO) {

        // URL의 ID를 DTO에 세팅하여 서비스로 전달
        customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("고객사 정보 수정에 성공했습니다."));
    }

    // 5. 고객사 비활성화 (삭제 대용) - 경로에 명확한 행위 추가
    @PatchMapping("/{customerId}/invalid")
    public ResponseEntity<ApiResponseDTO<Void>> invalidCustomer(@PathVariable Long customerId) {
        customerService.invalidCustomer(customerId);
        return ResponseEntity.ok(ApiResponseDTO.success("고객사가 비활성화되었습니다."));
    }

    // 6. 고객사 활성화 (복구) - 경로에 명확한 행위 추가
    @PatchMapping("/{customerId}/valid")
    public ResponseEntity<ApiResponseDTO<Void>> validCustomer(@PathVariable Long customerId) {
        customerService.validCustomer(customerId);
        return ResponseEntity.ok(ApiResponseDTO.success("고객사가 활성화되었습니다."));
    }
}