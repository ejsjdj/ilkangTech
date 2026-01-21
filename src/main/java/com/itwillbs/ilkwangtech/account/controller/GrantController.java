package com.itwillbs.ilkwangtech.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GrantController {

    @GetMapping("account/grant")
    public String grant() {
        return "/account/grant";
    }

}
