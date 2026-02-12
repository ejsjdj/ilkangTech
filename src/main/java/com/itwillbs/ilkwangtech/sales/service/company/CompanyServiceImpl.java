package com.itwillbs.ilkwangtech.sales.service.company;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.constant.CompanyStatus;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
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

    @Override
    @Transactional
    public void createCompany(CompanyDTO companyDTO) {
        if (companyRepository.findByName(companyDTO.getName()) != null)
            throw new IllegalArgumentException("이미 등록된 회사입니다.");
        companyDTO.setValid(CompanyStatus.ACTIVE);
        companyRepository.save(companyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompanyList(Pageable pageable, CompanyCategory category) {
        long offset = pageable.getOffset();
        long pageSize = pageable.getPageSize();
        return companyRepository.selectByPage(offset, pageSize, category);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCount(CompanyCategory category) {
        return companyRepository.selectCount(category);
    }

}