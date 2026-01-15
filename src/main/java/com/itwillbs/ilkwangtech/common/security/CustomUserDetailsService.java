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
		
		Member member = accountRepository.findByEmployeeNumber(employeeNumber)
				.orElseThrow(() -> new UsernameNotFoundException(employeeNumber + " : + 사용자 조회 실패!"));

		AccountLogin accountLoginDTO = modelMapper.map(member, AccountLogin.class);

		return accountLoginDTO;
	}

}
