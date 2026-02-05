package com.itwillbs.ilkwangtech.hr.controller;

import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import com.itwillbs.ilkwangtech.hr.dto.*;
import com.itwillbs.ilkwangtech.hr.service.*;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
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
    private final LeaveAllService leaveAllService;
    private final RequestUpdateCommuteService requestUpdateCommuteService;


    // 휴가 관리 페이지 이동
    @GetMapping("/vacation")
    public String getVacation(){

        return "hr/vacation";
    }

    // 휴가 목록 조회
    @GetMapping("/vacation/list")
    @ResponseBody
    public List<LeaveDTO> getVacationList(@AuthenticationPrincipal AccountLogin accountLogin,
                                          @RequestParam(name = "workDate") LocalDate workDate){

        System.out.println(accountLogin.getId());

        List<LeaveDTO> DTO = leaveService.getLeaveStatus(accountLogin.getId(), workDate);

        System.out.println(DTO);

        return DTO;
    }

    // 부서별 휴가 목록 조회
    @GetMapping("/vacation/all")
    @ResponseBody
    public List<LeaveByDepartmentDTO> getVacationAll(@AuthenticationPrincipal AccountLogin accountLogin,
                                                     @RequestParam(name = "workDate") LocalDate workDate){

        System.out.println("부서: " + accountLogin.getDepartment());

        return leaveAllService.getLeaveAllList(accountLogin.getDepartment(), workDate);

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
    public List<CommuteDTO> getCommuteList(@AuthenticationPrincipal AccountLogin accountLogin,
                                           @RequestParam LocalDate workDate){
        return commuteService.getPersonalCommuteList(accountLogin.getId(), workDate);
    }

    // 부서별 출퇴근 기록 조회
    @GetMapping("/commute/all")
    @ResponseBody
    public List<CommuteAllDTO> getCommuteAllList(@RequestParam(name = "deptCode") Integer deptCode,
                                                 @RequestParam(name = "workDate") LocalDate workDate
                                                 ){

        return commuteAllService.getCommuteAllList(deptCode, workDate);

    }

    // 출퇴근 기록 수정 요청
    @PostMapping("/commute/request")
    @ResponseBody
    public void requestUpdateCommuteStatus(@RequestParam(name = "attendanceId", required = false) Long attendanceId,
                                           @RequestParam(name = "context", required = false) String context,
                                           @RequestParam(name = "contextDetail", required = false) String contextDetail,
                                           @RequestParam(name = "targetDate", required = false) LocalDate targetDate){

        System.out.println(attendanceId + context + contextDetail + targetDate);

        requestUpdateCommuteService.requestUpdateCommute(attendanceId, context, contextDetail, targetDate);
    }

    // 출퇴근 기록 수정
    @PostMapping("/commute/update")
    @ResponseBody
    public void updateCommuteStatus(@AuthenticationPrincipal AccountLogin accountLogin,
                                    @RequestParam(name = "attendanceId", required = false) Long attendanceId,
                                    @RequestParam(name = "inTime", required = false) String inTime,
                                    @RequestParam(name = "outTime", required = false) String outTime,
                                    @RequestParam(name = "goOutTime", required = false) String goOutTime,
                                    @RequestParam(name = "returnTime", required = false) String returnTime){

        updateCommuteService.updateCommuteService(accountLogin.getId(), attendanceId, inTime, outTime, goOutTime, returnTime);
    }

    // 근무 현황 페이지 이동
    @GetMapping("/work")
    public String getWorkStatus(Model model){

        model.addAttribute("departments", departmentService.getActiveDepartments());

        return "hr/workStatus";
    }

    // 전 사원 근무 현황 조회
    @GetMapping("/work/list")
    @ResponseBody
    public List<WorkStatusDTO> getWorkList(@RequestParam(name = "deptCode") Integer deptCode,
                                           @RequestParam(name = "workDate") LocalDate workDate){

        System.out.println("근무현황 부서 코드 : " + deptCode);
        System.out.println("근무현황 일자 : " + workDate);

        return workStatusService.getWorkStatus(deptCode, workDate);
    }

    // 특정 사원의 퇴근 처리
    @PostMapping("/work/updateGoOut")
    @ResponseBody
    public AttendanceDTO updateGoOut(@RequestParam("userId") long userId){
        return updateWorkStatusService.updateGoOutService(userId);
    }

    // 특정 사원의 외근 복귀 처리
    @PostMapping("/work/updateComeBack")
    @ResponseBody
    public AttendanceDTO updateComeBack(@RequestParam("userId") long userId){
        return updateWorkStatusService.updateComeBackService(userId);
    }
}