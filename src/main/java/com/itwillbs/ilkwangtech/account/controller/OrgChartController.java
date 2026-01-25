package com.itwillbs.ilkwangtech.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrgChartController {

    @GetMapping("/account/orgChart")
    public String organizationPage() {
        return "/account/orgChart";
    }

}
