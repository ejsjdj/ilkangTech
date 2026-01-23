package com.itwillbs.ilkwangtech.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    @GetMapping("/")
    public String mainPage() {
        return "/schedule/calendar";
    }

}
