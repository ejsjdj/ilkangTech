package com.itwillbs.ilkwangtech.Hr.service;

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

    @Transactional
    public void getCommuteList(Long userId){

        // Attendance commuteEntity = attendanceRepository.findByMemberId(userId);

        // return

    }

}
