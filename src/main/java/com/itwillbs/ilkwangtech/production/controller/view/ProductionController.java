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

    @GetMapping("/instruct")
    public String getProcessInstructList() {
        return "production/productionInstruct";
    }

    @GetMapping("/production_register")
    public String getProcessRegister() {
        return "production/productionRegister";
    }


}
