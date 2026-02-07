package com.itwillbs.ilkwangtech.common.service;

import com.itwillbs.ilkwangtech.account.entity.Bank;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.BankRepository;
import com.itwillbs.ilkwangtech.account.repository.CommonCodeRepository;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.common.entity.CommonCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final BankRepository bankRepository;

    @Override
    @Cacheable(value = "departments", key = "#id", unless = "#result == null")
    public String getDepartmentName(Integer id) {
        if (id == null) return null;
        return departmentRepository.findById(id).map(Department::getDepartmentName).orElse(null);
    }

    @Override
    @CacheEvict(value = "departments", key = "#id")
    public void updateDepartment(Integer id, String newName) {
        Department dept = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다."));
        if (newName != null) dept.setDepartmentName(newName);
        departmentRepository.save(dept);
    }

    @Override
    @Cacheable(value = "positions", key = "#id", unless = "#result == null")
    public String getPositionName(Integer id) {
        if (id == null) return null;
        return positionRepository.findById(id).map(Position::getPositionName).orElse(null);
    }

    @Override
    @CacheEvict(value = "positions", key = "#id")
    public void updatePosition(Integer id, String newName) {
        Position pos = positionRepository.findById(id).orElseThrow(() -> new RuntimeException("직책을 찾을 수 없습니다."));
        if (newName != null) pos.setPositionName(newName);
        positionRepository.save(pos);
    }

    @Override
    @Cacheable(value = "banks", key = "#id", unless = "#result == null")
    public String getBankName(Integer id) {
        if (id == null) return null;
        return bankRepository.findById(id).map(Bank::getBankName).orElse(null);
    }

    @Override
    @CacheEvict(value = "banks", key = "#id")
    public void updateBank(Integer id, String newName) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("은행을 찾을 수 없습니다."));
        if (newName != null) bank.setBankName(newName);
        bankRepository.save(bank);
    }

    @Override
    @Cacheable(value = "roles", key = "#id", unless = "#result == null")
    public String getRoleName(Long id) {
        if (id == null) return null;
        return commonCodeRepository.findById(id).map(CommonCode::getCommonCodeName).orElse(null);
    }

    @Override
    @CacheEvict(value = "roles", key = "#id")
    public void updateRole(Long id, String newName) {
        CommonCode commonCode = commonCodeRepository.findById(id).orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다."));
        if (newName != null) commonCode.setCommonCodeName(newName);
        commonCodeRepository.save(commonCode);
    }


}
