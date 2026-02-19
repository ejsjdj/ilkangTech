package com.itwillbs.ilkwangtech.standard.controller.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/process")
public class RoutingController {

    @GetMapping("/list")
    public String getProcessList() {
        return "standard/processList";
    }
}
