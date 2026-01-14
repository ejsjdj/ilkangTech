package com.itwillbs.ilkwangtech.account.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwillbs.ilkwangtech.account.dto.AccountDTO;
import com.itwillbs.ilkwangtech.account.dto.AccountLoginDTO;
import com.itwillbs.ilkwangtech.account.service.AccountService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 회원가입페이지 요청
    @GetMapping("/create")
    public String create() {return "account/create";}

    // 회원가입요청
    @PostMapping("/create")
    // 입력값이 올바르게 들어왔는데 AccountDTO 에 Validation 어노테이션으로 확인
    // 잘못된 값이 있으면 경고메시지를 출력
    public String create(@Valid AccountDTO req) {
    	// 회원가입 값이 올바르게 입력되었다면 service 의 회원가입 처리 메서드를 호출
        accountService.create(req);
        return "/account/login";
    }
    
    // 로그인 페이지 요청
    @GetMapping("/login")
    public String login() {return "/account/login";}

    // 로그인 요청
    @PostMapping("/login")
    // 로그인 요청페이지에서 사원번호와 패스워드를 입력 받고 세션에 저장하기 위해 해당 세션을 지정
//    public String login(String employeeNumber, String password, HttpSession session) {
//        AccountLoginDTO dto = accountService.login(req);
//        session.setAttribute("loginMember", dto);
//        return "redirect:/account/list";
//    }

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

        Page<AccountDTO> page = accountService.getListPage(pageable);
        model.addAttribute("list", page.getContent());
        model.addAttribute("page", page);

        return "/account/list";
    }
    
    

}
