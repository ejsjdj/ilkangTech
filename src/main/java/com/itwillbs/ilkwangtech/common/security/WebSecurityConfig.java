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
	private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 접근 권한 설정
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/account/login", "/css/**", "/js/**", "/img/**", "/error").permitAll()
						.anyRequest().authenticated()
				);

		http
				.formLogin(formLogin -> formLogin
						.loginPage("/account/login")
						.loginProcessingUrl("/account/login")
						.usernameParameter("username")
						.passwordParameter("password")
						.successHandler(authenticationSuccessHandler)
						.failureHandler(authenticationFailureHandler)
						.permitAll()
				);

		http
	            .logout(logout -> logout
	                    .logoutUrl("/logout")
	                    .logoutSuccessUrl("/account/login")
	                    .invalidateHttpSession(true)
	                    .deleteCookies("JSESSIONID")
	                    .permitAll()
	            );

		http
				.csrf(csrf -> csrf.disable());

		return http.build();
	}
}
