package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Departments, Long> {

    // 활성화된 부서 목록을 가져오는 메서드
    List<Departments> findByIsActiveTrue();

    // 부서명으로 해당 부서의 정보를 가져오는 메서드
    Optional<Departments> findByDepartmentName(String departmentName);

}
