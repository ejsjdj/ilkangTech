package com.itwillbs.ilkwangtech.sales.service.company;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.constant.CompanyStatus;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import com.itwillbs.ilkwangtech.sales.mapper.CompanyMapper;
import com.itwillbs.ilkwangtech.sales.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional
    public void createCompany(CompanyDTO companyDTO) {
        if (companyRepository.findByName(companyDTO.getCompanyName()) != null)
            throw new IllegalArgumentException("이미 등록된 회사입니다.");
        companyRepository.save(companyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompanyList(CompanyCategory companyType, Pageable pageable) {
        long offset = pageable.getOffset();
        long pageSize = pageable.getPageSize();
        return companyMapper.selectByPage(offset, pageSize, companyType);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCount(CompanyCategory companyType) {
        return companyMapper.selectCount(companyType);
    }

}