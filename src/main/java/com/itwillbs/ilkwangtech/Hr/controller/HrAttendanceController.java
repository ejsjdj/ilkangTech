package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.LeaveDTO;
import com.itwillbs.ilkwangtech.Hr.service.CommuteService;
import com.itwillbs.ilkwangtech.Hr.service.LeaveService;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class HrAttendanceController {

    private final LeaveService leaveService;
    private final CommuteService commuteService;

    // 휴가 조회
    @GetMapping("/vacation")
    @ResponseBody
    public List<LeaveDTO> getVacationManage(@AuthenticationPrincipal AccountLogin accountLogin){
        Long userId = accountLogin.getId();

        return leaveService.getLeaveStatus(userId);

    }

    // 개인 출퇴근 현황 조회
    @GetMapping("/commute")
    @ResponseBody
    public void getCommuteStatus(@AuthenticationPrincipal AccountLogin accountLogin){
        Long userId = accountLogin.getId();
        commuteService.getCommuteList(userId);

    }



    // 근무 현황 조회
}