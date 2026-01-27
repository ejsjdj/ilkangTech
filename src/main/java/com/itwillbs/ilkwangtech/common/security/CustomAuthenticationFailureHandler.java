package com.itwillbs.ilkwangtech.common.security;

import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.LoginAttemptRepository;
import com.itwillbs.ilkwangtech.account.service.LoginAttemptService;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
@Log4j2
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final AccountRepository accountRepository;
    private final LoginAttemptService loginAttemptService;
    private final LoginAttemptRepository loginAttemptRepository;

    public CustomAuthenticationFailureHandler(AccountRepository accountRepository, LoginAttemptService loginAttemptService, LoginAttemptRepository loginAttemptRepository) {
        this.accountRepository = accountRepository;
        this.loginAttemptService = loginAttemptService;
        this.loginAttemptRepository = loginAttemptRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String username = request.getParameter("username");

        Member member = accountRepository.findByEmployeeNumberWithMemberRoles(username)
                .orElse(null);

        if (member != null) {
            // 없으면 새로 만들고, 있으면 그대로 가져오는 메서드 사용
            LoginAttempt loginAttempt = loginAttemptService.getLoginAttempt(member.getId());

            // 실패 횟수 증가 + 잠금 처리
            loginAttempt.increaseFailedCount(5);
            loginAttemptRepository.save(loginAttempt);

            String errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
            if (loginAttempt.isLocked()) {
                errorMessage = "로그인 5회 실패로 계정이 잠겼습니다. 관리자에게 문의하세요.";
            } else if (exception instanceof LockedException || exception instanceof DisabledException || exception instanceof AccountExpiredException || exception instanceof CredentialsExpiredException) {
                errorMessage = "잠긴 계정입니다. 관리자에게 문의하세요.";
            }
            response.sendRedirect("/account/login?error=true&message=" + URLEncoder.encode(errorMessage, "UTF-8"));
        } else {
            response.sendRedirect("/account/login?error=true&message=" + URLEncoder.encode("존재하지 않는 사원번호입니다.", "UTF-8"));
        }
    }
}
