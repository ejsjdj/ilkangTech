package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.hr.dto.LeaveByDepartmentDTO;
import com.itwillbs.ilkwangtech.hr.dto.LeaveDTO;
import com.itwillbs.ilkwangtech.hr.entity.LeaveEntity;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.hr.repository.LeaveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 부서별 휴가 현황 조회
@Service
@AllArgsConstructor
public class LeaveAllService {

    private DraftRepository draftRepository;
    private final DepartmentRepository departmentRepository;

    public List<LeaveByDepartmentDTO> getLeaveAllList(String department, LocalDate workDate){

        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("부서 정보가 입력되지 않았습니다. 다시 로그인해주세요");
        }

        Department deptCode = departmentRepository.findByDepartmentName(department)
                .orElseThrow(() -> new IllegalArgumentException("부서코드가 부적절합니다. 시스템 관리자에게 문의하세요."));

        LocalDate start = workDate.withDayOfMonth(1);
        LocalDate end = workDate.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

        return draftRepository.findLeaveSummaryByDept(deptCode.getId(), start, end);
    }
}
