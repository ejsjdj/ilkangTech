package com.itwillbs.ilkwangtech.Hr.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.Hr.constant.AttendanceStatus;
import com.itwillbs.ilkwangtech.Hr.dto.AttendanceDTO;
import com.itwillbs.ilkwangtech.Hr.entity.Attendance;
import com.itwillbs.ilkwangtech.Hr.repository.AttendanceRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a hh:mm");
    
    // 1. 현재 상태 조회
    @Transactional(readOnly = true)
    public AttendanceDTO getTodayStatus(Long memberId) {
    	log.info("getTodayStatus() 실행!");
    	log.info("getTodayStatus() 종료!");
        return attendanceRepository.findByMemberIdAndWorkDate(memberId, LocalDate.now())
                .map(AttendanceDTO::fromEntity)
                .orElse(AttendanceDTO.builder().status(AttendanceStatus.OFF_DUTY).build());
    }

    // 2. 출근 처리
    public AttendanceDTO clockIn(Long memberId) {
    	log.info("clockIn() 실행!");
        // 이미 출근 기록이 있는지 체크
        if(attendanceRepository.findByMemberIdAndWorkDate(memberId, LocalDate.now()).isPresent()){
            throw new IllegalStateException("이미 출근 처리되었습니다.");
        }

        Member member = memberRepository.findById(memberId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();

        Attendance attendance = Attendance.builder()
                .member(member)
                .workDate(LocalDate.now())
                .inTime(now)
                .status(AttendanceStatus.ON_DUTY)
                .build();

        attendanceRepository.save(attendance);

        String message = String.format("반갑습니다 %s %s님! %s 분 출근입니다.",
                member.getName(), member.getPosition(), now.format(timeFormatter));

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        log.info("clockIn() 종료!");
        return dto;
    }

    // 3. 퇴근 처리
    public AttendanceDTO clockOut(Long memberId) {
    	log.info("clockOut() 실행!");

        Attendance attendance = getTodayAttendance(memberId);
        LocalDateTime now = LocalDateTime.now();
        Member member = attendance.getMember();

        attendance.recordClockOut(now);

        String message = String.format("%s %s님! %s 분 퇴근입니다. 오늘 하루도 고생하셨습니다.",
                member.getName(), member.getPosition(), now.format(timeFormatter));

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        log.info("clockOut() 종료!");
        return dto;
    }

    // 4. 외근/휴식 등록 
    public AttendanceDTO goOutside(Long memberId, AttendanceDTO params) {
    	log.info("goOutside() 실행!");
        Attendance attendance = getTodayAttendance(memberId);
        LocalDateTime now = LocalDateTime.now();
        Member member = attendance.getMember();

        attendance.updateOutsideInfo(params.getStatus(), params.getMemo());
        
        String reason = params.getStatus() == AttendanceStatus.OUT_WORK ? "외근" : "휴식";

        String message = String.format("%s %s님! %s(%s) 사유로 %s분에 등록되었습니다.",
                member.getName(), member.getPosition(), reason, params.getMemo(), now.format(timeFormatter));

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        log.info("goOutside() 종료!");
        return dto;
    }

    // 5. 복귀 처리
    public AttendanceDTO comeBack(Long memberId) {
    	log.info("comeBack() 실행!");
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
