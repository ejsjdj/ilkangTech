package com.itwillbs.ilkwangtech.Hr.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.ilkwangtech.Hr.constant.AttendanceStatus;
import com.itwillbs.ilkwangtech.Hr.dto.AttendanceDTO;
import com.itwillbs.ilkwangtech.Hr.service.AttendanceService;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Log4j2
public class AttendanceApiController {

    private final AttendanceService attendanceService;
    
    // 시간 포맷 
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a hh:mm");

    // 1. 상태 조회
    @GetMapping("/status")
    public ResponseEntity<AttendanceDTO> getStatus(@AuthenticationPrincipal AccountLogin loginMember) {
    	log.info("getStatus() 실행!");
    	log.info("getStatus() 종료!");
        return ResponseEntity.ok(attendanceService.getTodayStatus(loginMember.getId()));
    }

    // 2. 출근
    @PostMapping("/in")
    public ResponseEntity<AttendanceDTO> clockIn(@AuthenticationPrincipal AccountLogin loginMember) {
    	log.info("clockIn() 실행!");
    	try {
            AttendanceDTO result = attendanceService.clockIn(loginMember.getId());
            
            String time = LocalDateTime.now().format(timeFormatter);
            String message = String.format("반갑습니다 %s %s님! %s 출근입니다.", 
                    loginMember.getName(), loginMember.getPosition(), time);
            
            result.setMessage(message); // 성공 메시지 설정
            log.info("clockIn() 종료(출근 성공)!");
            return ResponseEntity.ok(result);

        } catch (IllegalStateException e) {
            AttendanceDTO errorResult = new AttendanceDTO();
            errorResult.setMessage(e.getMessage()); 
            
            log.info("clockIn() 종료(중복 출근)!");
            return ResponseEntity.ok(errorResult);
        }
    }

    // 3. 퇴근
    @PostMapping("/out")
    public ResponseEntity<AttendanceDTO> clockOut(@AuthenticationPrincipal AccountLogin loginMember) {
    	log.info("clockOut() 실행!");
        AttendanceDTO result = attendanceService.clockOut(loginMember.getId());

        String time = LocalDateTime.now().format(timeFormatter);
        String message = String.format("수고하셨습니다 %s %s님! %s 분 퇴근 처리되었습니다.", 
                loginMember.getName(), loginMember.getPosition(), time);
        
        result.setMessage(message);

        log.info("clockOut() 종료!");
        return ResponseEntity.ok(result);
    }

    // 4. 외근 등록
    @PostMapping("/outside")
    public ResponseEntity<AttendanceDTO> goOutside(@RequestBody AttendanceDTO params, 
                                                   @AuthenticationPrincipal AccountLogin loginMember) {
    	log.info("goOutside() 실행!");
        AttendanceDTO result = attendanceService.goOutside(loginMember.getId(), params);

        String time = LocalDateTime.now().format(timeFormatter);
        String reason = params.getMemo(); // 사유
        String message = String.format("%s %s님! 외근(%s) 사유로 %s분에 등록되었습니다.", 
                loginMember.getName(), loginMember.getPosition(), reason, time);
        
        result.setMessage(message);

        log.info("goOutside() 종료!");
        return ResponseEntity.ok(result);
    }

    // 5. 복귀
    @PostMapping("/return")
    public ResponseEntity<AttendanceDTO> comeBack(@AuthenticationPrincipal AccountLogin loginMember) {
    	log.info("comeBack() 실행!");
        AttendanceDTO result = attendanceService.comeBack(loginMember.getId());

        String time = LocalDateTime.now().format(timeFormatter);
        String message = String.format("%s %s님! %s 분 복귀입니다. 어서오세요!", 
                loginMember.getName(), loginMember.getPosition(), time);
        
        result.setMessage(message);

        log.info("comeBack() 종료!");
        return ResponseEntity.ok(result);
    }
}