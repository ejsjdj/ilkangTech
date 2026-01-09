package com.itwillbs.ilkwangtech.account.controller;

import org.springframework.ui.Model;
import com.itwillbs.ilkwangtech.account.dto.AccountDTO;
import com.itwillbs.ilkwangtech.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/join")
    public String join() {
        return "/join";
    }

    @PostMapping("/join")
    public String join(@Valid AccountDTO.AccountJoinRequest req,
                       Model model) {

        System.out.println(req.name());
        System.out.println(req.gender());
        System.out.println(req.hireDate());
        System.out.println(req.residentNumber());
        System.out.println(req.email());
        System.out.println(req.phoneNumber());
        System.out.println(req.password());
        System.out.println(req.department());
        System.out.println(req.position());
        System.out.println(req.bank());
        System.out.println(req.accountNumber());

        return "index";  // 뷰 이름 또는 리다이렉트
    }

    @PostMapping("/login")
    public String login(@Valid AccountDTO.AccountLoginRequest req) {



        return "join";
    }



}
