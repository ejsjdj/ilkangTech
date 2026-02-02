package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.entity.CommuteUpdateRequestEntity;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import com.itwillbs.ilkwangtech.hr.repository.CommuteUpdateRequestRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

// 근태시간 수정 요청
@Service
@RequiredArgsConstructor
public class RequestUpdateCommuteService {

    private final CommuteUpdateRequestRepository commuteUpdateRequestRepository;
    private final AttendanceRepository attendanceRepository;


    public void requestUpdateCommute(Long attendanceId, String context, String contextDetail, LocalDate targetDate){
        CommuteUpdateRequestEntity commuteUpdateRequestEntity = new CommuteUpdateRequestEntity();

        commuteUpdateRequestEntity.setNewDate(attendanceId, context, contextDetail, targetDate);

        commuteUpdateRequestRepository.save(commuteUpdateRequestEntity);

        // TODO :: 서비스 전 attendanceId 타입 Attendance로 변경!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Optional<Attendance> attendance = attendanceRepository.findById(attendanceId);

    }
}
