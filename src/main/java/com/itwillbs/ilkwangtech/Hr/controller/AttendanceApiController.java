package com.itwillbs.ilkwangtech.Hr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.ilkwangtech.Hr.dto.AttendanceDTO;
import com.itwillbs.ilkwangtech.Hr.service.AttendanceService;
import com.itwillbs.ilkwangtech.account.dto.AccountDTO.AccountLoginResponse;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceApiController {

    private final AttendanceService attendanceService;

    // 1. 상태 조회
    @GetMapping("/status")
    public ResponseEntity<AttendanceDTO> getStatus(HttpSession session) {
        return ResponseEntity.ok(attendanceService.getTodayStatus(getMemberId(session)));
    }

    // 2. 출근
    @PostMapping("/in")
    public ResponseEntity<AttendanceDTO> clockIn(HttpSession session) {
        return ResponseEntity.ok(attendanceService.clockIn(getMemberId(session)));
    }

    // 3. 퇴근
    @PostMapping("/out")
    public ResponseEntity<AttendanceDTO> clockOut(HttpSession session) {
        return ResponseEntity.ok(attendanceService.clockOut(getMemberId(session)));
    }

    // 4. 외근 등록
    @PostMapping("/outside")
    public ResponseEntity<AttendanceDTO> goOutside(@RequestBody AttendanceDTO params, HttpSession session) {
        return ResponseEntity.ok(attendanceService.goOutside(getMemberId(session), params));
    }

    // 5. 복귀
    @PostMapping("/return")
    public ResponseEntity<AttendanceDTO> comeBack(HttpSession session) {
        return ResponseEntity.ok(attendanceService.comeBack(getMemberId(session)));
    }

    // 세션에서 MemberDTO의 ID 추출
    private Long getMemberId(HttpSession session) {
    	AccountLoginResponse loginMember = (AccountLoginResponse) session.getAttribute("loginMember");
        if (loginMember == null) throw new IllegalStateException("로그인이 필요합니다.");
        return loginMember.id();
    }

}
