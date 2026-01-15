package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    // 활성화된 부서 목록을 가져오는 메서드
    List<Department> findByIsActiveTrue();

    // 부서명으로 해당 부서의 정보를 가져오는 메서드
    Optional<Department> findByDepartmentName(String departmentName);

}
