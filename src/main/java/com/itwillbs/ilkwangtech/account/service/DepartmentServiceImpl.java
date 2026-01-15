package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getActiveDepartments() {
        return departmentRepository.findByIsActiveTrue();
    }
}
