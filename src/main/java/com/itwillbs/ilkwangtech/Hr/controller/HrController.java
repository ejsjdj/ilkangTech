package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.constant.DepartmentType;
import com.itwillbs.ilkwangtech.Hr.constant.RankType;
import com.itwillbs.ilkwangtech.Hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.Hr.service.AppointmentService;
import com.itwillbs.ilkwangtech.Hr.service.RegistAppointmentService;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import com.itwillbs.ilkwangtech.account.service.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 인사관리 메뉴탭
@Controller
@RequestMapping("/hr")
@AllArgsConstructor
public class HrController {

    private final AppointmentService appointmentService;
    private final RegistAppointmentService registAppointmentService;
    private final DepartmentService departmentService;
    private final PositionService positionService;

    // 발령 관리
    @GetMapping("/appointment")
    public String getAppointment(){
        return "hr/appointment";
    }

    // 발령 리스트 조회
    @GetMapping("/appointment/list")
    @ResponseBody
    public List<AppointmentDTO> getAppointmentList(){
        return appointmentService.hrAppointmentService();
    }

    // 발령 등록 페이지 이동
    @GetMapping("/appointment/insert")
    public String getAppointmentInsert(Model model){

        // 부서 데이터 조회 및 전달
        model.addAttribute("departments", departmentService.getActiveDepartments());

        // 직급 데이터 조회 및 전달
        model.addAttribute("positions", positionService.getActivePositions());

        return "hr/appointmentInsert";
    }

    // 발령 등록
    @GetMapping("/appointment/insertData")
    public String registAppointmentInsert(@AuthenticationPrincipal AccountLogin accountLogin,
                                        @RequestParam(name = "userId", required = false) Long userId,
                                        @RequestParam(name = "newDept", required = false) Integer newDept,
                                        @RequestParam(name = "newRank", required = false) Integer newRank,
                                        @RequestParam(name = "workStatus", required = false) String workStatus){

        System.out.println("발령 정보 -> " + " approverId : " + accountLogin.getId() + ", newDept : " + newDept + ", newRank : " + newRank);

        Long approverId = accountLogin.getId();
        registAppointmentService.registAppointment(approverId, userId, newDept, newRank, workStatus);

        return "redirect:/hr/appointment";
    }

    // 조직도
    @GetMapping("/organization")
    public String getOrganization(){
        return "hr/organization";
    }
}
