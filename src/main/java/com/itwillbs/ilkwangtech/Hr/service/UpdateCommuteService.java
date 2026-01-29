package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.entity.Attendance;
import com.itwillbs.ilkwangtech.Hr.repository.AttendanceRepository;
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
    public void updateCommuteService(long attendanceId, LocalDateTime inTime, LocalDateTime outTime){

        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();

        if(inTime != null) {
            attendance.changeInTime(inTime);
        }
        if (outTime != null) {
            attendance.changeOutTime(outTime);
        }
    }
}
