package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.hr.dto.CommuteAllDTO;
import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 부서별 출퇴근 현황 조회
@Service
@AllArgsConstructor
public class CommuteAllService {

    private final AttendanceRepository attendanceRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;




    @Transactional
    public List<CommuteAllDTO> getCommuteAllList(Integer dept, LocalDate date) {

        List<Attendance> allList = attendanceRepository.findByDepartmentAndDate(dept, date);

        return allList.stream().map(
                attendance -> CommuteAllDTO.builder().
                        id(attendance.getId()).
                        name(attendance.getMember().getName()).
                        workDate(attendance.getWorkDate()).
                        inTime(attendance.getInTime()).
                        goOutTime(attendance.getGoOutTime()).
                        outTime(attendance.getOutTime()).
                        returnTime(attendance.getReturnTime()).
                        build()).toList();

    }
}
