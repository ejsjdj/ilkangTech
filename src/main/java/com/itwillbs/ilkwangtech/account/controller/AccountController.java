package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountRegisterRequest;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponse;
import com.itwillbs.ilkwangtech.account.service.AccountService;
import com.itwillbs.ilkwangtech.account.service.BankService;
import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import com.itwillbs.ilkwangtech.account.service.PositionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final BankService bankService;
    private final AccountService accountService;

    // 회원가입페이지 요청
    @GetMapping("/register")
    public String register(Model model) {

        // 부서 데이터
        model.addAttribute("departments", departmentService.getActiveDepartments());

        // 직급 데이터
        model.addAttribute("positions", positionService.getActivePositions());

        // 은행 데이터
        model.addAttribute("banks", bankService.getActiveBanks());

        return "account/register";
    }

    // 회원가입요청
    @PostMapping("/register")
    public String register(Model model, AccountRegisterRequest req) {
        AccountRegisterResponse res = accountService.register(req);
        model.addAttribute("response",res);
        return "/account/register";
    }

    // 로그인 페이지 요청
    @GetMapping("/login")
    public String login() { return "/account/login"; }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다");
        return "redirect:/";
    }

    @GetMapping("/myInfo")
    public String update(Model model) {
        model.addAttribute("user", null);
        return "/account/myInfo";

    }
}