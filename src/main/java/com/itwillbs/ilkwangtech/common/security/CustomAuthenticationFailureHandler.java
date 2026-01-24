package com.itwillbs.ilkwangtech.common.security;

import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.LoginAttemptRepository;
import com.itwillbs.ilkwangtech.account.service.AccountService;
import com.itwillbs.ilkwangtech.account.service.LoginAttemptService;
import com.itwillbs.ilkwangtech.common.exception.MemberNotFoundException;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

        Member member = accountRepository.getMemberByEmployeeNumber(username)
                .orElseThrow(() -> new MemberNotFoundException());

        // 없으면 새로 만들고, 있으면 그대로 가져오는 메서드 사용
        LoginAttempt loginAttempt = loginAttemptService.getLoginAttempt(member.getId());

        // 실패 횟수 증가 + 잠금 처리
        loginAttempt.increaseFailedCount(5);

        loginAttemptRepository.save(loginAttempt);

        response.sendRedirect("/account/login");
    }
}
