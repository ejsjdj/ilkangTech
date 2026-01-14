package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.entity.Departments;
import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import com.itwillbs.ilkwangtech.account.service.DepartmentServiceImpl;
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

import java.util.List;


@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final DepartmentService departmentService;
    // 회원가입페이지 요청
    @GetMapping("/register")
    public String register(Model model) {
        List<Departments> departments = departmentService.getActiveDepartments();
        model.addAttribute("departments", departments);
        return "account/register";
    }

    // 회원가입요청
    @PostMapping("/register")
    // 입력값이 올바르게 들어왔는데 AccountDTO 에 Validation 어노테이션으로 확인
    // 잘못된 값이 있으면 경고메시지를 출력
    public String create(@Valid AccountDTO req) {
    	// 회원가입 값이 올바르게 입력되었다면 service 의 회원가입 처리 메서드를 호출
//        accountService.create(req);
        return "/account/register";
    }
    
    // 로그인 페이지 요청
    @GetMapping("/login")
    public String login() {return "/account/login";}

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

//        Page<AccountDTO> page = accountService.getListPage(pageable);
//        model.addAttribute("list", page.getContent());
//        model.addAttribute("page", page);

        return "/account/list";
    }
    
    

}
