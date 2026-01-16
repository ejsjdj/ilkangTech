package com.itwillbs.ilkwangtech.common.security;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	// DB 를 참조하기 위한 accountRepository 선언
	private final AccountRepository accountRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {
		log.info("========== loadUserByUsername 시작 ==========");
		log.info("검색할 사원번호: {}", employeeNumber);
		Member member = accountRepository.findByEmployeeNumberWithMemberRoles(employeeNumber)
				.orElseThrow(() -> new UsernameNotFoundException(employeeNumber + " : + 사용자 조회 실패!"));

		log.info("✅ 사용자 조회 성공");
		log.info("  - 이름: {}", member.getName());
		log.info("  - 이메일: {}", member.getEmail());
		log.info("  - DB에 저장된 비밀번호 (첫 20글자): {}",
				member.getPassword() == null ? "null" :
						member.getPassword().substring(0, Math.min(20, member.getPassword().length())));
				AccountLogin accountLogin = modelMapper.map(member, AccountLogin.class);
		log.info("========== loadUserByUsername 종료 ==========");
		return accountLogin;
	}
}
