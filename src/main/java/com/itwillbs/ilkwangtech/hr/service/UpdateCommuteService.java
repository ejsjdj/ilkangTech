package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 출퇴근 시간 수정
@Service
@AllArgsConstructor
public class UpdateCommuteService {

    private final AttendanceRepository attendanceRepository;

    // 출퇴근 시간 조정
    @Transactional
    public void updateCommuteService(Long userId, Long attendanceId, String inTime, String outTime, String goOutTime, String returnTime){

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("출퇴근 기록이 존재하지 않습니다."));

        LocalDate baseDate = attendance.getInTime().toLocalDate();

        // 1. 출근시간 조정
        if(inTime != null && !inTime.isEmpty()) {
            attendance.changeInTime(inTime, baseDate);
        }

        // 2. 퇴근시간 조정
        if (outTime != null && !outTime.isEmpty()) {
            attendance.changeOutTime(outTime, baseDate);
        }

        // 3. 외근 시간 조정
        if (goOutTime != null && !goOutTime.isEmpty()) {
            attendance.changeGoOutTime(goOutTime, baseDate);
        }

        // 4. 복귀 시간 조정
        if (returnTime != null && !returnTime.isEmpty()) {
            attendance.changeReturnTime(returnTime, baseDate);
        }

        // 5. 승인자 정보 저장
        attendance.setApprover(userId);
    }
}
