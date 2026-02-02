package com.itwillbs.ilkwangtech.hr.service;

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

    public List<LeaveByDepartmentDTO> getLeaveAllList(Integer department, LocalDate workDate){

        LocalDate start = workDate.withDayOfMonth(1);
        LocalDate end = workDate.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

        return draftRepository.findLeaveSummaryByDept(department, start, end);
    }
}
