package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.entity.Departments;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    DepartmentRepository departmentRepository;

    @Override
    public List<Departments> getActiveDepartments() {
        return departmentRepository.findByIsActiveTrue();
    }
}
