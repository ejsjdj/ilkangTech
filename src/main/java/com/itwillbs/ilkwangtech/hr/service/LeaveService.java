package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.LeaveDTO;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.hr.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;

    @Transactional
    public List<LeaveDTO> getLeaveStatus(Long userId, LocalDate workDate){

        LocalDate start = workDate.withDayOfMonth(1);
        LocalDate end = workDate.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

        return leaveRepository.findVacationAndLeaveDetails(userId, start, end);
    }
}
