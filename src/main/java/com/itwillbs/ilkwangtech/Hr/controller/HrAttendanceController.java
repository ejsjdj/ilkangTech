package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.LeaveDTO;
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

    // 휴가 조회
    @GetMapping("/vacation")
    @ResponseBody
    public List<LeaveDTO> getVacationManage(@AuthenticationPrincipal AccountLogin accountLogin){
        Long userId = accountLogin.getId();

        return leaveService.getLeaveStatus(userId);

    }
}