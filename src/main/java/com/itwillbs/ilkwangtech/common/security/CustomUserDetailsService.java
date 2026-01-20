package com.itwillbs.ilkwangtech.common.security;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	// DB 를 참조하기 위한 accountRepository 선언
	private final AccountRepository accountRepository;
	private final ModelMapper modelMapper;
	
	// 부서, 직급 관련 주입
	private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
	
	@Override
	public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {

		Member member = accountRepository.findByEmployeeNumberWithMemberRoles(employeeNumber)
				.orElseThrow(() -> new UsernameNotFoundException(employeeNumber + " : + 사용자 조회 실패!"));

		AccountLogin accountLogin = modelMapper.map(member, AccountLogin.class);
		
		// 부서 이름 변환
        int deptId = member.getDepartment(); 
        if (deptId > 0) { 
            Department dept = departmentRepository.findById(deptId).orElse(null);
            
            if (dept != null) {
                // 로그인 후 AccountLogin 에서 문자로 변환
                accountLogin.setDepartment(dept.getDepartmentName()); 
            }
        }

        // 직급 이름 변환
        int posId = member.getPosition(); 
        if (posId > 0) { 
            Position pos = positionRepository.findById(posId).orElse(null);
            
            if (pos != null) {
                // 로그인 후 AccountLogin 에서 문자로 변환
                accountLogin.setPosition(pos.getPositionName());
            }
        }

        log.info("최종 변환된 부서: {}", accountLogin.getDepartment());
        log.info("최종 변환된 직급: {}", accountLogin.getPosition());

		return accountLogin;
	}
}
