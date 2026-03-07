package com.itwillbs.ilkwangtech.production.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/production")
public class ProductionController {

    @GetMapping("/production_list")
    public String getProcessList() {
            return "production/productionList";
        }

    @GetMapping("/production_register")
    public String getProcessRegister() {
        return "production/productionRegister";
    }

    @GetMapping("/instruct_list")
    public String getProcessInstructList() {
        return "production/productionInstruct";
    }

    @GetMapping("/instruct_register")
    public String getProcessInstructRegister() {
        return "production/productionInstructRegister";
    }

    @GetMapping("/production_dashboard")
    public String getProcessDashboard() {
        return "production/productionDashboard";
    }


}
