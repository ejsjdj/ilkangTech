package com.itwillbs.ilkwangtech.account.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/create")
    public String create() {
        return "account/create";
    }

    @PostMapping("/create")
    public String join(@Valid AccountDTO.AccountJoinRequest req) {
        accountService.create(req);
        return "/account/login";
    }

    @GetMapping("/login")
    public String login() {
        return "/account/login";
    }

    @PostMapping("/login")
    public String login(@Valid AccountDTO.AccountLoginRequest req, HttpSession session) {
        AccountDTO.AccountLoginResponse res = accountService.login(req);
        session.setAttribute("loginMember", res);
        System.out.println("로그인 완료");
        return "redirect:/account/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다");
        System.out.println("로그아웃 완료");
        return "redirect:/";  // ← 데이터와 함께 전달
    }

    // 페이징 목록 (권장)
    @GetMapping("/list")
    public String listPage(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model) {

        Page<AccountDTO.AccountListResponse> page = accountService.getListPage(pageable);
        model.addAttribute("list", page.getContent());
        model.addAttribute("page", page);

        return "/account/list";
    }

}
