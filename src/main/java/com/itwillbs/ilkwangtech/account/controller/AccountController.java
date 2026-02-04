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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.IOException;


/**
 * 계정 관련 요청을 처리하는 컨트롤러
 * 회원가입, 로그인, 로그아웃, 내 정보 수정 기능을 담당합니다.
 */
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final BankService bankService;
    private final AccountService accountService;

    /**
     * 회원가입 페이지를 요청합니다.
     * 부서, 직급, 은행 목록 데이터를 모델에 담아 전달합니다.
     *
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 회원가입 페이지 뷰 경로
     */
    @GetMapping("/register")
    public String register(Model model) {

        // 부서 데이터 조회 및 전달
        model.addAttribute("departments", departmentService.getActiveDepartments());

        // 직급 데이터 조회 및 전달
        model.addAttribute("positions", positionService.getActivePositions());

        // 은행 데이터 조회 및 전달
        model.addAttribute("banks", bankService.getActiveBanks());

        return "account/register";
    }

    /**
     * 회원가입 요청을 처리합니다.
     *
     * @param req 회원가입 정보가 담긴 DTO
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 성공 시 사원 목록 페이지로, 실패 시 회원가입 페이지로 리다이렉트
     */
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

    /**
     * 로그인 페이지를 요청합니다.
     *
     * @return 로그인 페이지 뷰 경로
     */
    @GetMapping("/login")
    public String login() { return "/account/login"; }

    /**
     * 로그아웃을 처리합니다.
     *
     * @param session 현재 세션 객체
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 메인 페이지로 리다이렉트
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // 세션 무효화
        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다");
        return "redirect:/";
    }

    /**
     * 내 정보 수정 페이지를 요청합니다.
     *
     * @param user 현재 로그인한 사용자 정보 (@AuthenticationPrincipal)
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 내 정보 수정 페이지 뷰 경로
     */
    @GetMapping("/myInfo")
    public String update(@AuthenticationPrincipal AccountLogin user, Model model) {
        model.addAttribute("user", user);
        return "/account/myInfo";
    }

    @PostMapping("/update/profileImgFile")
    public String updateProfileImgFile(@AuthenticationPrincipal AccountLogin user,
                                       @RequestParam("profileImgFile") MultipartFile profileImgFile,
                                       RedirectAttributes rttr) throws IOException {

        int row = accountService.updateProfileImage(profileImgFile, user);

        rttr.addFlashAttribute(row > 0 ? "successMessage" : "errorMessage", 
                               row > 0 ? "프로필 이미지가 변경되었습니다." : "프로필 이미지 변경에 실패했습니다.");

        return "redirect:/account/myInfo";
    }

//    @PostMapping("/updateProfileImage")
//    public String updateProfileImage(@RequestParam MultipartFile upload, HttpSession session, RedirectAttributes rttr) {
//        System.out.println(upload);
//        int row = memberService.updateProfileImage(upload, session);
//        rttr.addFlashAttribute("message", row > 0 ? "프로필 이미지 수정 성공" : "프로필 이미지 수정 실패");
//        return "redirect:/member/info";
//    }
//

    /**
     * 내 정보를 수정합니다.
     *
     * @param user 현재 로그인한 사용자 정보
     * @param id 수정할 회원의 고유 ID
     * @param email 수정할 이메일
     * @param phoneNumber 수정할 전화번호
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 내 정보 수정 페이지로 리다이렉트
     */
    @PostMapping("/update")
    public String updateMyInfo(@AuthenticationPrincipal AccountLogin user,
                               Long id,
                               String email,
                               String phoneNumber,
                               RedirectAttributes redirectAttributes) {
        
        // 본인 여부 확인 (IDOR 방지: 요청받은 ID가 로그인한 사용자의 ID와 일치하는지 검증)
        if (!accountService.isSelf(id, user.getEmployeeNumber())) {
            redirectAttributes.addFlashAttribute("errorMessage", "잘못된 접근입니다.");
            return "redirect:/account/myInfo";
        }

        boolean success = accountService.updateMyInfo(id, email, phoneNumber);

        if (success) {
            // 성공 시 현재 로그인 세션 객체의 정보도 갱신
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            redirectAttributes.addFlashAttribute("successMessage", "내 정보가 수정되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "정보 수정에 실패했습니다.");
        }

        return "redirect:/account/myInfo";
    }
}