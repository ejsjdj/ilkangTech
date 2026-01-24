package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.CommuteDTO;
import com.itwillbs.ilkwangtech.Hr.dto.LeaveDTO;
import com.itwillbs.ilkwangtech.Hr.dto.WorkStatusDTO;
import com.itwillbs.ilkwangtech.Hr.service.CommuteService;
import com.itwillbs.ilkwangtech.Hr.service.LeaveService;
import com.itwillbs.ilkwangtech.Hr.service.UpdateWorkStatusService;
import com.itwillbs.ilkwangtech.Hr.service.WorkStatusService;
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
    private final WorkStatusService workStatusService;
    private final UpdateWorkStatusService updateWorkStatusService;

    // 휴가 조회
    @GetMapping("/vacation")
    @ResponseBody
    public List<LeaveDTO> getVacationManage(@AuthenticationPrincipal AccountLogin accountLogin){
        // TODO :: 부서별 휴가 현황, 전 사원 휴가 현황 조회 기능 추가할 것
        return leaveService.getLeaveStatus(accountLogin.getId());
    }

    // 개인 출퇴근 현황 조회
    @GetMapping("/commute")
    @ResponseBody
    public List<CommuteDTO> getCommuteStatus(@AuthenticationPrincipal AccountLogin accountLogin){
        /* TODO :: 전 사원 출퇴근 현황, 출퇴근 상태 수정 추가할 것
        * TODO :: 지각이면 시각적으로 표시할 수 있는 기능 추가할 것
        * */
        return commuteService.getCommuteList(accountLogin.getId());

    }

    // 근무 현황 조회
    @GetMapping("/status")
    @ResponseBody
    public List<WorkStatusDTO> getWorkStatus(){
        // TODO :: 전 사원 실시간 근무 현황 조회 기능 추가할 것

        return workStatusService.getWorkStatus();
    }

    // 근무 현황 수정
    @PostMapping("/status/update")
    @ResponseBody
    public void postWorkStatus(@RequestParam("userId") long userId,
                               @RequestParam("workStatus") String workStatus){
        // TODO :: 출근 상태 수정 기능 추가할 것
        updateWorkStatusService.updateWorkService(userId, workStatus);
    }
}