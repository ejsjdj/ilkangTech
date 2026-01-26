package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterRequest;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponse;
import com.itwillbs.ilkwangtech.account.service.AccountService;
import com.itwillbs.ilkwangtech.account.service.BankService;
import com.itwillbs.ilkwangtech.account.service.DepartmentService;
import com.itwillbs.ilkwangtech.account.service.PositionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String register(AccountRegisterRequest req, RedirectAttributes redirectAttributes) {
        AccountRegisterResponse res = accountService.register(req);
        if(res.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", "사원 등록이 완료되었습니다. 사원번호: " + res.getEmployeeNumber());
            return "redirect:/account/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", res.getMessage());
            return "redirect:/account/register";
        }
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
    public String update(@AuthenticationPrincipal AccountLogin user, Model model) {
        model.addAttribute("user", user);
        return "/account/myInfo";
    }

    @PostMapping("/update")
    public String updateMyInfo(@AuthenticationPrincipal AccountLogin user,
                               String email,
                               String phoneNumber,
                               RedirectAttributes redirectAttributes) {
        boolean success = accountService.updateMyInfo(user.getId(), email, phoneNumber);

        if (success) {
            // 세션 정보 갱신 (선택 사항: 현재는 다시 로그인하거나 정보를 다시 로드해야 함)
            // 여기서는 간단히 성공 메시지만 전달
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            redirectAttributes.addFlashAttribute("successMessage", "내 정보가 수정되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "정보 수정에 실패했습니다.");
        }

        return "redirect:/account/myInfo";
    }
}