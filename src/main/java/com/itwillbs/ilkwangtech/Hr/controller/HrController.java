package com.itwillbs.ilkwangtech.Hr.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 인사관리 메뉴탭
@Controller
@RequestMapping("/hr")
public class HrController {

//  인사 조회
    @GetMapping("/emp")
    public String getMember(){
        return "인사조회";
    }

//  발령 관리
    @GetMapping("/appointment")
    public String getAppointment(){
        return "hr/appointment";
    }

//  발령 등록
    @GetMapping("/appointment/insert")
    public String getAppointmentInsert(){



        return "hr/appointmentInsert";
    }

//  조직도
    @GetMapping("/organization")
    public String getOrganization(){
        return "hr/organization";
    }
}
