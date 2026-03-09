package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountDetailResponse;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterRequestDTO;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponseDTO;
import com.itwillbs.ilkwangtech.account.service.*;
import com.itwillbs.ilkwangtech.common.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private final ListService listService;
    private final AuditLogService auditLogService;

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
    public String register(AccountRegisterRequestDTO req, RedirectAttributes redirectAttributes) {
        AccountRegisterResponseDTO res = accountService.register(req);
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
     * @return 내 정보 수정 페이지 뷰 경로
     */
    @GetMapping("/myInfo")
    public String update(@AuthenticationPrincipal AccountLogin user, Model model) {

        AccountDetailResponse detail = listService.getAccountDetail(user.getId());
        model.addAttribute("user", detail);
        return "/account/myInfo";
    }

    // 마이페이지에서 프로필
    @PostMapping("/update/profileImgFile")
    public String updateProfileImgFile(@AuthenticationPrincipal AccountLogin user,
                                       @RequestParam("profileImgFile") MultipartFile profileImgFile,
                                       RedirectAttributes rttr) throws IOException {

        int row = accountService.updateProfileImage(user, profileImgFile);

        rttr.addFlashAttribute(row > 0 ? "successMessage" : "errorMessage", 
                               row > 0 ? "프로필 이미지가 변경되었습니다." : "프로필 이미지 변경에 실패했습니다.");

        return "redirect:/account/myInfo";
    }

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
    @Transactional
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

        if (user.updateInfo(email, phoneNumber) && accountService.updateMyInfo(id, email, phoneNumber))
            redirectAttributes.addFlashAttribute("successMessage", "내 정보가 수정되었습니다.");

        else
            redirectAttributes.addFlashAttribute("errorMessage", "정보 수정에 실패했습니다.");

        return "redirect:/account/myInfo";
    }

    /**
     * 사원의 비밀번호를 '1234'로 초기화합니다. (관리자 전용)
     */
    @PreAuthorize("hasAnyAuthority('CEO', 'HR')")
    @PostMapping("/resetPassword")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestParam Long id) {
        if (accountService.resetPassword(id)) {
            return ResponseEntity.ok("비밀번호가 '1234'로 초기화되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("비밀번호 초기화에 실패했습니다.");
        }
    }

    /**
     * 사용자가 직접 비밀번호를 변경합니다.
     */
    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changePassword(@AuthenticationPrincipal AccountLogin user,
                                                              @RequestParam String oldPassword,
                                                              @RequestParam String newPassword) {
        Map<String, Object> response = new HashMap<>();
        if (accountService.changePassword(user.getId(), oldPassword, newPassword)) {
            response.put("success", true);
            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "기존 비밀번호가 일치하지 않거나 변경에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 비밀번호 찾기(본인 확인) 페이지를 요청합니다.
     */
    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "account/forgotPassword";
    }

    /**
     * 본인 확인을 통해 비밀번호를 재설정합니다.
     */
    @PostMapping("/forgotPassword")
    public String resetPasswordBySelf(@RequestParam String employeeNumber,
                                      @RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String newPassword,
                                      RedirectAttributes rttr) {
        if (accountService.verifyMember(employeeNumber, name, email)) {
            if (accountService.resetPassword(employeeNumber, newPassword)) {
                rttr.addFlashAttribute("successMessage", "비밀번호가 재설정되었습니다. 새로운 비밀번호로 로그인해주세요.");
                return "redirect:/account/login";
            } else {
                rttr.addFlashAttribute("errorMessage", "비밀번호 재설정에 실패했습니다.");
            }
        } else {
            rttr.addFlashAttribute("errorMessage", "입력하신 정보가 일치하지 않습니다.");
        }
        return "redirect:/account/forgotPassword";
    }

    /**
     * 활동 로그 페이지를 요청합니다. (관리자 전용)
     */
    @PreAuthorize("hasAnyAuthority('CEO', 'HR', 'INFORMATION')")
    @GetMapping("/audit-log")
    public String auditLog() {
        return "account/auditLog";
    }

    /**
     * 활동 로그 데이터를 조회합니다. (관리자 전용)
     */
    @PreAuthorize("hasAnyAuthority('CEO', 'HR', 'INFORMATION')")
    @GetMapping("/audit-log/data")
    @ResponseBody
    public ResponseEntity<List<com.itwillbs.ilkwangtech.common.entity.AuditLog>> getAuditLogData(
            @RequestParam(required = false) String employeeNumber) {
        
        List<com.itwillbs.ilkwangtech.common.entity.AuditLog> logs;
        if (employeeNumber != null && !employeeNumber.isEmpty()) {
            logs = auditLogService.findByEmployeeNumber(employeeNumber);
        } else {
            logs = auditLogService.findAll();
        }
        return ResponseEntity.ok(logs);
    }
}