package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
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

    public CompanyDTO findByName(String name) {
        return companyMapper.selectByName(name);
    }

    public List<CompanyDTO> selectByPage(Long offset, Long pageSize, CompanyCategory category) {
        return companyMapper.selectByPage(offset, pageSize, category);
    }

    public long selectCount(CompanyCategory category) {
        return companyMapper.selectCount(category);
    }

}