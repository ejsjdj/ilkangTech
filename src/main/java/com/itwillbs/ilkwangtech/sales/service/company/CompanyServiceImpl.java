package com.itwillbs.ilkwangtech.sales.service.company;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.constant.CompanyStatus;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseCompanyDTO;
import com.itwillbs.ilkwangtech.sales.repository.CompanyRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initTable() {
        log.info("Checking COMPANY table schema...");
        try {
            // Oracle은 기본적으로 대문자로 테이블/컬럼명을 관리함
            checkAndAddColumn("COMPANY", "BUSINESS_NUMBER", "VARCHAR2(20)");
            checkAndAddColumn("COMPANY", "FAX_NO", "VARCHAR2(20)");
            checkAndAddColumn("COMPANY", "EMAIL", "VARCHAR2(100)");
            checkAndAddColumn("COMPANY", "ADDRESS", "VARCHAR2(255)");
            checkAndAddColumn("COMPANY", "MANAGER_NAME", "VARCHAR2(50)");
            checkAndAddColumn("COMPANY", "MANAGER_TEL", "VARCHAR2(20)");
            checkAndAddColumn("COMPANY", "STATUS", "INTEGER DEFAULT 1");
        } catch (Exception e) {
            log.error("Failed to update COMPANY table schema: {}", e.getMessage());
        }
    }

    private void checkAndAddColumn(String tableName, String columnName, String columnDef) {
        String checkSql = "SELECT count(*) FROM user_tab_columns WHERE table_name = ? AND column_name = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, tableName.toUpperCase(), columnName.toUpperCase());

        if (count == null || count == 0) {
            log.info("Adding column {} to table {}...", columnName, tableName);
            String alterSql = String.format("ALTER TABLE %s ADD (%s %s)", tableName, columnName, columnDef);
            jdbcTemplate.execute(alterSql);
            log.info("Column {} added successfully.", columnName);
        }
    }

    @Override
    @Transactional
    public void createCompany(CompanyDTO companyDTO) {
        if (companyRepository.findByName(companyDTO.getCompanyName()) != null)
            throw new IllegalArgumentException("이미 등록된 회사입니다.");
        if (companyDTO.getStatus() == null) {
            companyDTO.setStatus(CompanyStatus.ACTIVE);
        }
        companyRepository.save(companyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompanyList(Pageable pageable, CompanyCategory companyType) {
        long offset = pageable.getOffset();
        long pageSize = pageable.getPageSize();
        return companyRepository.selectByPage(offset, pageSize, companyType);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCount(CompanyCategory companyType) {
        return companyRepository.selectCount(companyType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseCompanyDTO> selectPurchaseCompany() {
        return companyRepository.selectCompanyType3();
    }

}