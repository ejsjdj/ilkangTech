package com.itwillbs.ilkwangtech.common.security;

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

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String inputPassword = request.getParameter("password");
        String inputEmployeeNumber = request.getParameter("username");

        log.error("========== 로그인 실패 상세 진단 ==========");
        log.error("[입력 정보]");
        log.error("  사원번호: {}", inputEmployeeNumber);
        log.error("  비밀번호: '{}'", inputPassword);
        log.error("  비밀번호 길이: {}", inputPassword != null ? inputPassword.length() : 0);
        log.error("  비밀번호 첫글자: '{}'", inputPassword != null && inputPassword.length() > 0 ? inputPassword.charAt(0) : "없음");
        log.error("  비밀번호 마지막글자: '{}'", inputPassword != null && inputPassword.length() > 0 ? inputPassword.charAt(inputPassword.length() - 1) : "없음");

        // ✅ 비밀번호가 null이거나 비어있는지 확인
        if (inputPassword == null) {
            log.error("❌ 비밀번호가 null입니다!");
        } else if (inputPassword.isEmpty()) {
            log.error("❌ 비밀번호가 빈 문자열입니다!");
        } else if (inputPassword.trim().isEmpty()) {
            log.error("❌ 비밀번호가 공백만 포함합니다!");
        }

        log.error("[예외 정보]");
        log.error("  예외 클래스: {}", exception.getClass().getSimpleName());
        log.error("  예외 메시지: {}", exception.getMessage());

        if (exception instanceof BadCredentialsException) {
            log.error("  원인: PasswordEncoder.matches() 실패 (비밀번호 불일치)");
        } else if (exception instanceof UsernameNotFoundException) {
            log.error("  원인: 사용자를 찾을 수 없음");
        }

        log.error("======================================");
    }
}
