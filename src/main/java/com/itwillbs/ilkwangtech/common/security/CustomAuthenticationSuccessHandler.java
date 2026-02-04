package com.itwillbs.ilkwangtech.common.security;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.repository.LoginAttemptRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginAttemptRepository loginAttemptRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        AccountLogin accountLogin = (AccountLogin) authentication.getPrincipal();
        
        // 로그인 성공 시 실패 횟수 초기화
        loginAttemptRepository.findByMemberId(accountLogin.getId()).ifPresent(loginAttempt -> {
            loginAttempt.reset();
            loginAttemptRepository.save(loginAttempt);
        });

        response.sendRedirect("/");
    }
}
