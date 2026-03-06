package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import com.itwillbs.ilkwangtech.sales.dto.PageResponseDTO;
import com.itwillbs.ilkwangtech.sales.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/company")
@RequiredArgsConstructor
public class CompanyApiController {

    private final CompanyService companyService;

    // 1. 고객사 등록
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<Void>> createCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.createCompany(companyDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("고객사 등록에 성공했습니다."));
    }

    // 2. 고객사 목록 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<CompanyDTO>>> getCustomerList(Pageable pageable, @RequestParam CompanyCategory category) {
        List<CompanyDTO> list = companyService.getCompanyList(pageable, category);
        long total = companyService.getTotalCount(category);

        PageResponseDTO<CompanyDTO> pageData = new PageResponseDTO<>(
                list, total, (int) Math.ceil((double) total / pageable.getPageSize())
        );

        return ResponseEntity.ok(ApiResponseDTO.success(pageData));
    }
}