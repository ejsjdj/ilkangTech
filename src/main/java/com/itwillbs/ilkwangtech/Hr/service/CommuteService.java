package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.AttendanceDTO;
import com.itwillbs.ilkwangtech.Hr.dto.CommuteDTO;
import com.itwillbs.ilkwangtech.Hr.entity.Attendance;
import com.itwillbs.ilkwangtech.Hr.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 개인 출퇴근 현황 조회
@Service
@RequiredArgsConstructor
public class CommuteService {

    private final AttendanceRepository attendanceRepository;

    // 개인의 출퇴근 전체 현황 조회
    @Transactional
    public List<CommuteDTO> getCommuteList(Long userId){

         List<Attendance> commute = attendanceRepository.findByMemberId(userId);

         return commute.stream().
                 map(attendance -> CommuteDTO.builder().
                         inTime(attendance.getInTime()).
                         goOutTime(attendance.getGoOutTime()).
                         outTime(attendance.getOutTime()).
                         returnTime(attendance.getReturnTime()).
                         build()).
                 toList();
    }
}
