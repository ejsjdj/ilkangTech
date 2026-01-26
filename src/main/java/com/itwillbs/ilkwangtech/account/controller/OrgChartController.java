package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrgChartController {

    private final DepartmentService departmentService;

    @GetMapping("/account/orgChart")
    public String organizationPage(Model model) {
        model.addAttribute("orgData", departmentService.getOrgChartData());
        return "account/orgChart";
    }

}
