package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.entity.Departments;

import java.util.List;

// 활성화된 부서 목록 조회
// 부서 추가
// 부서 삭제
public interface DepartmentService {

    // 활성화된 부서 목록 조회
    public List<Departments> getActiveDepartments();
}
