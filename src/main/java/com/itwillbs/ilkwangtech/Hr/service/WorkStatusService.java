package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.WorkStatusDTO;
import com.itwillbs.ilkwangtech.Hr.entity.Attendance;
import com.itwillbs.ilkwangtech.Hr.repository.AttendanceRepository;
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
