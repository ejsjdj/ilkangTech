package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseCompanyDTO;
import com.itwillbs.ilkwangtech.sales.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CompanyRepository {

    private final CompanyMapper companyMapper;

    public void save(CompanyDTO companyDTO) {
        companyMapper.insertCompany(companyDTO);
    }

    public CompanyDTO findByName(String companyName) {
        return companyMapper.selectByName(companyName);
    }

    public List<CompanyDTO> selectByPage(Long offset, Long pageSize, CompanyCategory companyType) {
        return companyMapper.selectByPage(offset, pageSize, companyType);
    }

    public long selectCount(CompanyCategory companyType) {
        return companyMapper.selectCount(companyType);
    }

    public List<PurchaseCompanyDTO> selectCompanyType3(){
        return companyMapper.selectCompanyType3();
    }

}