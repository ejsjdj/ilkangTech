package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.AttendanceDTO;
import com.itwillbs.ilkwangtech.Hr.dto.CommuteDTO;
import com.itwillbs.ilkwangtech.Hr.dto.LeaveDTO;
import com.itwillbs.ilkwangtech.Hr.dto.WorkStatusDTO;
import com.itwillbs.ilkwangtech.Hr.service.*;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class HrAttendanceController {

    private final LeaveService leaveService;
    private final CommuteService commuteService;
    private final WorkStatusService workStatusService;
    private final UpdateWorkStatusService updateWorkStatusService;
    private final UpdateCommuteService updateCommuteService;

    // 휴가 조회
    @GetMapping("/vacation")
    @ResponseBody
    public List<LeaveDTO> getVacationManage(@AuthenticationPrincipal AccountLogin accountLogin){
        // TODO :: 부서별 휴가 현황 조회 필요
        return leaveService.getLeaveStatus(accountLogin.getId());
    }

    // 개인 출퇴근 기록 조회
    @GetMapping("/commute")
    @ResponseBody
    public List<CommuteDTO> getCommuteStatus(@AuthenticationPrincipal AccountLogin accountLogin){
        return commuteService.getPersonalCommuteList(accountLogin.getId());
    }

    // 전체 출퇴근 기록 조회
    @GetMapping("/allCommute")
    @ResponseBody
    public List<CommuteDTO> getAllCommuteStatus(){
        // TODO :: 부서명, 이름, 직급 반환 필요
        return commuteService.getAllCommuteList();
    }

    // 출퇴근 기록 수정
    @PostMapping("/commute/update")
    @ResponseBody
    public void updateCommuteStatus(@RequestParam("attendacneId") long attendanceId,
                                    @RequestParam("inTime") LocalDateTime inTime,
                                    @RequestParam("outTime") LocalDateTime outTime){

        updateCommuteService.updateCommuteService(attendanceId,inTime, outTime);
    }

    // 전 사원 근무 현황 조회
    @GetMapping("/status")
    @ResponseBody
    public List<WorkStatusDTO> getWorkStatus(){
        return workStatusService.getWorkStatus();
    }

    // 특정 사원의 퇴근 처리
    @PostMapping("/status/updateGoOut")
    @ResponseBody
    public AttendanceDTO updateGoOut(@RequestParam("userId") long userId){
        return updateWorkStatusService.updateGoOutService(userId);
    }

    // 특정 사원의 외근 복귀 처리
    @PostMapping("/status/updateComeBack")
    @ResponseBody
    public AttendanceDTO updateComeBack(@RequestParam("userId") long userId){
        return updateWorkStatusService.updateComeBackService(userId);
    }

}