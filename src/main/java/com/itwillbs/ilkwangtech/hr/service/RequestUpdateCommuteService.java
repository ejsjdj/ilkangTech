package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.entity.CommuteUpdateRequestEntity;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import com.itwillbs.ilkwangtech.hr.repository.CommuteUpdateRequestRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestUpdateCommuteService {

    private CommuteUpdateRequestRepository commuteUpdateRequestRepository;
    private MemberRepository memberRepository;
    private AttendanceRepository attendanceRepository;


    public void requestUpdateCommute(Long requesterId, Long attendanceId, String type, String context, String contextDetail, String file){
        CommuteUpdateRequestEntity commuteUpdateRequestEntity = new CommuteUpdateRequestEntity();

        Optional<Member> requester = memberRepository.findById(requesterId);
        Optional<Attendance> attendance = attendanceRepository.findById(attendanceId);

    }
}
