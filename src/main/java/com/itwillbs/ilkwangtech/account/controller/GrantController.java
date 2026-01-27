package com.itwillbs.ilkwangtech.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/account")
@RestController
public class GrantController {

    @GetMapping("/grant/{memberId}")
    public String grant() {



        return "/account/grant";
    }


}
