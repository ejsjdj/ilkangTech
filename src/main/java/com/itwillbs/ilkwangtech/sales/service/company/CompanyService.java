package com.itwillbs.ilkwangtech.sales.service.company;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseCompanyDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {

    void createCompany(CompanyDTO companyDTO);

    List<CompanyDTO> getCompanyList(Pageable pageable, CompanyCategory companyType);

    long getTotalCount(CompanyCategory companyType);

    List<PurchaseCompanyDTO> selectPurchaseCompany();

}
