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

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a hh:mm");
    
 // 1. 현재 상태 조회
    @Transactional(readOnly = true)
    public AttendanceDTO getTodayStatus(Long memberId) {
        return attendanceRepository.findByMemberIdAndWorkDate(memberId, LocalDate.now())
                .map(AttendanceDTO::fromEntity)
                .orElse(AttendanceDTO.builder().status(AttendanceStatus.OFF_DUTY).build());
    }

    // 2. 출근 처리
    public AttendanceDTO clockIn(Long memberId) {
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
        return dto;
    }

    // 3. 퇴근 처리
    public AttendanceDTO clockOut(Long memberId) {
        Attendance attendance = getTodayAttendance(memberId);
        LocalDateTime now = LocalDateTime.now();
        Member member = attendance.getMember();

        attendance.recordClockOut(now);

        String message = String.format("%s %s님! %s 분 퇴근입니다. 오늘 하루도 고생하셨습니다.",
                member.getName(), member.getPosition(), now.format(timeFormatter));

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        return dto;
    }

    // 4. 외근/휴식 등록 (DTO에서 status, memo 받음)
    public AttendanceDTO goOutside(Long memberId, AttendanceDTO params) {
        Attendance attendance = getTodayAttendance(memberId);
        LocalDateTime now = LocalDateTime.now();
        Member member = attendance.getMember();

        // params.getStatus()는 화면에서 선택한 OUT_WORK 또는 LEAVE
        attendance.updateOutsideInfo(params.getStatus(), params.getMemo());
        
        // 사유 텍스트 (알림용)
        String reason = params.getStatus() == AttendanceStatus.OUT_WORK ? "외근" : "휴식";

        String message = String.format("%s %s님! %s(%s) 사유로 %s분에 등록되었습니다.",
                member.getName(), member.getPosition(), reason, params.getMemo(), now.format(timeFormatter));

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        return dto;
    }

    // 5. 복귀 처리
    public AttendanceDTO comeBack(Long memberId) {
        Attendance attendance = getTodayAttendance(memberId);
        LocalDateTime now = LocalDateTime.now();
        Member member = attendance.getMember();

        attendance.changeStatus(AttendanceStatus.ON_DUTY); // 다시 근무 상태로

        String message = String.format("%s %s님! %s 분 복귀입니다. 어서오세요!",
                member.getName(), member.getPosition(), now.format(timeFormatter));

        AttendanceDTO dto = AttendanceDTO.fromEntity(attendance);
        dto.setMessage(message);
        return dto;
    }

    private Attendance getTodayAttendance(Long memberId) {
        return attendanceRepository.findByMemberIdAndWorkDate(memberId, LocalDate.now())
                .orElseThrow(() -> new IllegalArgumentException("출근 기록이 없습니다."));
    }

}
