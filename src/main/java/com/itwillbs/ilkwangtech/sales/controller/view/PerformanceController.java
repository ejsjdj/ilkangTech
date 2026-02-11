package com.itwillbs.ilkwangtech.sales.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/performance")
public class PerformanceController {

    @GetMapping("/list")
    public String getPerformanceList() {
        return "sales/performance/list";
    }


}
