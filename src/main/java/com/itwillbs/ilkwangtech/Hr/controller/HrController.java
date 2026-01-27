package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.Hr.service.AppointmentService;
import com.itwillbs.ilkwangtech.Hr.service.RegistAppointmentService;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

// 인사관리 메뉴탭
@Controller
@RequestMapping("/hr")
@AllArgsConstructor
public class HrController {

    private final AppointmentService appointmentService;
    private final RegistAppointmentService registAppointmentService;

    // 발령 관리
    @GetMapping("/appointment")
    @ResponseBody
    public List<AppointmentDTO> getAppointment(){
        return appointmentService.hrAppointmentService();
    }

    // 발령 등록 페이지 이동
    @GetMapping("/appointment/insert")
    public String getAppointmentInsert(){
        return "hr/appointmentInsert";
    }

    // 발령 등록
    @GetMapping("/appointment/insert/test")
    public void registAppointmentInsert(@AuthenticationPrincipal AccountLogin accountLogin,
                                        @RequestParam("userId") Long userId,
                                        @RequestParam("newDept") int newDept,
                                        @RequestParam("newRank") int newRank,
                                        @RequestParam("workStatus") String workStatus){

        Long approverId = accountLogin.getId();
        registAppointmentService.registAppointment(approverId, userId, newDept, newRank, workStatus);

    }

    // 조직도
    @GetMapping("/organization")
    public String getOrganization(){
        return "hr/organization";
    }
}
