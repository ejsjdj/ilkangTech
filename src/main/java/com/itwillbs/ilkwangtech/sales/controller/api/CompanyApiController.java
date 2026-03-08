package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PageResponseDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseCompanyDTO;
import com.itwillbs.ilkwangtech.sales.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/company")
@RequiredArgsConstructor
public class CompanyApiController {

    private final CompanyService companyService;

    // 1. 고객사 등록
    @PreAuthorize("hasAnyAuthority('CEO', 'SALES', 'PURCHASING')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<Void>> createCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.createCompany(companyDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("고객사 등록에 성공했습니다."));
    }

    // 2. 고객사 목록 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<CompanyDTO>>> getCustomerList(
            @PageableDefault(page = 0, size = 10, sort = "companyId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam CompanyCategory companyType) {
        List<CompanyDTO> list = companyService.getCompanyList(pageable, companyType);
        long total = companyService.getTotalCount(companyType);

        PageResponseDTO<CompanyDTO> pageData = new PageResponseDTO<>(
                list, total, (int) Math.ceil((double) total / pageable.getPageSize())
        );
        return ResponseEntity.ok(ApiResponseDTO.success(pageData));
    }

    // 3. 거래처만 조회
    @GetMapping("/list/purchase_company")
    public ResponseEntity<ApiResponseDTO<List<PurchaseCompanyDTO>>> getPurchaseCompany(){

        List<PurchaseCompanyDTO> listData = companyService.selectPurchaseCompany();

        return ResponseEntity.ok(ApiResponseDTO.success(listData));
    }

    // 4. 상세 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CompanyDetailDTO>> getCompanyDetail(@PathVariable Long id) {
        CompanyDetailDTO detail = companyService.getCompanyDetail(id);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponseDTO.success(detail));
    }

    // 5. 판매 내역 페이징 조회
    @GetMapping("/{id}/sales")
    public ResponseEntity<ApiResponseDTO<org.springframework.data.domain.Page<com.itwillbs.ilkwangtech.sales.dto.OrderDTO>>> getSalesHistory(
            @PathVariable Long id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(companyService.getSalesHistory(id, pageable)));
    }

    // 6. 구매 내역 페이징 조회
    @GetMapping("/{id}/purchases")
    public ResponseEntity<ApiResponseDTO<org.springframework.data.domain.Page<com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO>>> getPurchaseHistory(
            @PathVariable Long id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(companyService.getPurchaseHistory(id, pageable)));
    }
}