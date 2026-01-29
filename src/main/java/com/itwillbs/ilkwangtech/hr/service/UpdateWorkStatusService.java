package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.AttendanceDTO;
import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.log4j.Log4j2;


// 근무 상태 업데이트
@Service
@Log4j2
public class UpdateWorkStatusService {

    private AttendanceRepository attendanceRepository;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a hh:mm");

    // 퇴근 처리
    @Transactional
    public AttendanceDTO updateGoOutService(long userId){

        LocalDateTime now = LocalDateTime.now();

        Attendance attendance = getTodayAttendance(userId);
        
        attendance.recordClockOut(now);

        Member member = attendance.getMember();

        // 외근 복귀 눌러주고 -> 퇴근 처리

        String message = String.format("%s %s님의 퇴근처리가 완료되었습니다.",
                member.getName(), member.getPosition());

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        log.info("clockOut() 종료!");
        return dto;
    }


    // 5. 복귀 처리
    @Transactional
    public AttendanceDTO updateComeBackService(Long memberId) {
        Attendance attendance = getTodayAttendance(memberId);
        LocalDateTime now = LocalDateTime.now();
        Member member = attendance.getMember();

        attendance.recordReturn(); // 다시 근무 상태로

        String message = String.format("%s %s님! %s 분 복귀입니다. 어서오세요!",
                member.getName(), member.getPosition(), now.format(timeFormatter));

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        log.info("comeBack() 종료!");
        return dto;
    }


    private Attendance getTodayAttendance(Long memberId) {
        log.info("getTodayAttendance() 실행!");
        log.info("getTodayAttendance() 종료!");
        return attendanceRepository.findByMemberIdAndWorkDate(memberId, LocalDate.now())
                .orElseThrow(() -> new IllegalArgumentException("출근 기록이 없습니다."));
    }
}
