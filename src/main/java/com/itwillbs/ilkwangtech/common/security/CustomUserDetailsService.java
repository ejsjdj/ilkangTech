package com.itwillbs.ilkwangtech.common.security;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.account.dto.AccountLoginDTO;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
	// DB 를 참조하기 위한 accountRepository 선언
	private final AccountRepository accountRepository;
	// 생성자 주입방식으로 의존성 주입
	public CustomUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {
		
		Member member = accountRepository.findByEmployeeNumber(employeeNumber)
				.orElseThrow(() -> new UsernameNotFoundException(employeeNumber + " : + 사용자 조회 실패!"));
		
		ModelMapper modelMapper = new ModelMapper();
		AccountLoginDTO accountLoginDTO = modelMapper.map(member, AccountLoginDTO.class);
		log.info("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆ accountLoginResponse 가 문제없이 작동하는가 마는가? : " + accountLoginDTO.getUsername());
		
		
		return accountLoginDTO;
	}
	
	

}
