package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UpdateCommuteService {

    private final AttendanceRepository attendanceRepository;

    // 출퇴근 시간 조정
    @Transactional
    public void updateCommuteService(long attendanceId, LocalDateTime inTime, LocalDateTime outTime, LocalDateTime goOutTime, LocalDateTime returnTime){

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("출퇴근 기록이 존재하지 않습니다."));

        // 1. 출근시간 조정
        if(inTime != null) {
            attendance.changeInTime(inTime);
        } else {
            attendance.changeInTime(attendance.getInTime());
        }

        // 2. 퇴근시간 조정
        if (outTime != null) {
            attendance.changeOutTime(outTime);
        } else {
            attendance.changeOutTime(attendance.getOutTime());
        }

        // 3. 외근 시간 조정

        if (goOutTime != null) {
            attendance.changeGoOutTime(goOutTime);
        } else {
            attendance.changeGoOutTime(attendance.getGoOutTime());
        }

        // 4. 복귀 시간 조정

        if (returnTime != null) {
            attendance.changeReturnTime(returnTime);
        } else {
            attendance.changeReturnTime(attendance.getReturnTime());
        }
    }
}
