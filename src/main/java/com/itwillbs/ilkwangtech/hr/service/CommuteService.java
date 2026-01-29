package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.CommuteDTO;
import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//  출퇴근 현황 조회
@Service
@RequiredArgsConstructor
public class CommuteService {

    private final AttendanceRepository attendanceRepository;

    // 개인 출퇴근 전체 현황 조회
    @Transactional
    public List<CommuteDTO> getPersonalCommuteList(Long userId){

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