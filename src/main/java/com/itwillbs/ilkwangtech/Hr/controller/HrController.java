package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.Hr.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

// 인사관리 메뉴탭
@Controller
@RequestMapping("/hr")
@AllArgsConstructor
public class HrController {

    private final AppointmentService appointmentService;

//  발령 관리
    @GetMapping("/appointment")
    @ResponseBody
    public List<AppointmentDTO> getAppointment(){
        return appointmentService.hrAppointmentService();
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
