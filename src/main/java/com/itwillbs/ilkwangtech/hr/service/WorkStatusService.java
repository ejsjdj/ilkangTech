package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.WorkStatusDTO;
import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkStatusService {

    private AttendanceRepository attendanceRepository;

    public List<WorkStatusDTO> getWorkStatus(){

        List<Attendance> workStatus = attendanceRepository.findAllWithMemberAndDepartments();

        return workStatus.stream()
                .map(attendance -> WorkStatusDTO.builder().
                        name(attendance.getMember().getName()).
                        // department(attendance.getMember().getDepartment()).
                        // position(attendance.getMember().getPosition()).
                        inTime(attendance.getInTime()).
                        goOutTime(attendance.getGoOutTime()).
                        outTime(attendance.getOutTime()).
                        returnTime(attendance.getReturnTime()).
                        // status(attendance.getStatus()).
                        build())
                .toList();
    }
}
