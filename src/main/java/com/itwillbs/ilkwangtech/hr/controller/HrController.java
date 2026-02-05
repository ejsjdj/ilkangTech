package com.itwillbs.ilkwangtech.hr.controller;

import com.itwillbs.ilkwangtech.hr.constant.DepartmentType;
import com.itwillbs.ilkwangtech.hr.constant.RankType;
import com.itwillbs.ilkwangtech.hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.hr.service.AppointmentService;
import com.itwillbs.ilkwangtech.hr.service.RegistAppointmentService;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import com.itwillbs.ilkwangtech.account.service.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public String getAppointment(Model model, @AuthenticationPrincipal AccountLogin accountLogin){

        System.out.println("보유 권한 : " + accountLogin.getRoles().get(0).getRole().getCommonCode());

        List<String> roleList = accountLogin.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        model.addAttribute("myRoles", roleList);

        return "hr/appointment";
    }

    // 발령 리스트 조회
    @GetMapping("/appointment/list")
    @ResponseBody
    public Page<AppointmentDTO> getAppointmentList(Pageable pageable,
                                                   @RequestParam(name = "searchField", required = false) String searchField){

        return appointmentService.hrAppointmentService(pageable, searchField);
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


    // 조직도
    @GetMapping("/organization")
    public String getOrganization(){
        return "hr/organization";
    }
}
