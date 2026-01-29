package com.itwillbs.ilkwangtech.hr.controller;

import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import com.itwillbs.ilkwangtech.hr.dto.*;
import com.itwillbs.ilkwangtech.hr.service.*;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final CommuteAllService commuteAllService;
    private final DepartmentService departmentService;

    // 휴가 조회
    @GetMapping("/vacation")
    @ResponseBody
    public List<LeaveDTO> getVacationManage(@AuthenticationPrincipal AccountLogin accountLogin){
        // TODO :: 부서별 휴가 현황 조회 필요
        return leaveService.getLeaveStatus(accountLogin.getId());
    }


    // 출퇴근 현황 페이지 이동
    @GetMapping("/commute")
    public String getCommuteStatus(Model model){
        // 부서 데이터 조회 및 전달
        model.addAttribute("departments", departmentService.getActiveDepartments());

        return "hr/commute";
    }

    // 개인 출퇴근 기록 조회
    @GetMapping("/commute/list")
    @ResponseBody
    public List<CommuteDTO> getCommuteList(@AuthenticationPrincipal AccountLogin accountLogin){
        return commuteService.getPersonalCommuteList(accountLogin.getId());
    }

    // 부서별 출퇴근 기록 조회
    @GetMapping("/commute/all")
    @ResponseBody
    public List<CommuteAllDTO> getCommuteAllList(@RequestParam(name = "deptCode") Integer deptCode,
                                                 @RequestParam(name = "workDate") LocalDate workDate){

        System.out.println("부서 코드: " + deptCode);
        System.out.println("근무일: " + workDate);

        return commuteAllService.getCommuteAllList(deptCode, workDate);

    }

    // 출퇴근 기록 수정
    @PostMapping("/commute/update")
    @ResponseBody
    public void updateCommuteStatus(@RequestParam(name = "attendanceId", required = false) Long attendanceId,
                                    @RequestParam(name = "inTime", required = false) LocalDateTime inTime,
                                    @RequestParam(name = "outTime", required = false) LocalDateTime outTime,
                                    @RequestParam(name = "outTime", required = false) LocalDateTime goOutTime,
                                    @RequestParam(name = "outTime", required = false) LocalDateTime returnTime){

        System.out.println(attendanceId);
        System.out.println(inTime);
        System.out.println(outTime);
        System.out.println(goOutTime);
        System.out.println(returnTime);

        updateCommuteService.updateCommuteService(attendanceId, inTime, outTime, goOutTime, returnTime);
    }

    // 전 사원 근무 현황 조회
    @GetMapping("/status")
    @ResponseBody
    public void getWorkStatus(){
        // return workStatusService.getWorkStatus();
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