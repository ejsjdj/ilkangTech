package com.itwillbs.ilkwangtech.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

	private final CustomAuthenticationFailureHandler authenticationFailureHandler;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				// 접근 권한 설정
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers("/account/login", "/account/register", "/css/**", "/js/**", "/img/**", "/error").permitAll()
//						.anyRequest().authenticated()
						.anyRequest().permitAll()
				)
				// 로그인 설정
				.formLogin(formLogin -> formLogin
						.loginPage("/account/login")
						.loginProcessingUrl("/account/login")
						.usernameParameter("username")
						.passwordParameter("password") // 기본값이
						.defaultSuccessUrl("/schedule/calendar", true)
						.failureHandler(authenticationFailureHandler)
						.permitAll() // 로그인 관련 요청 주소를 모두 허용 경로로 등록
				)
				.csrf(csrf -> csrf.disable())
				
				// 로그아웃 설정
	            .logout(logout -> logout
	                    .logoutUrl("/logout")              
	                    .logoutSuccessUrl("/account/login") // 로그아웃 성공 시 로그인 페이지로 이동
	                    .invalidateHttpSession(true)       
	                    .deleteCookies("JSESSIONID")       
	                    .permitAll()
	            )
				.build();
	}
}
