package com.itwillbs.ilkwangtech.common.security;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.entity.Bank;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.*;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	// DB 를 참조하기 위한 accountRepository 선언
	private final AccountRepository accountRepository;
	private final ModelMapper modelMapper;
	
	// 부서, 직급, 은행 관련 주입
	private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
	private final BankRepository bankRepository;
	
	@Override
	public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {
		
		// 회원 정보 조회
		Member member = accountRepository.findByEmployeeNumberWithMemberRoles(employeeNumber)
				.orElseThrow(() -> new UsernameNotFoundException(employeeNumber + " : + 사용자 조회 실패!"));

		// 매핑 수행
		AccountLogin accountLogin = modelMapper.map(member, AccountLogin.class);
		
		// 부서 이름 변환
        if (member.getDepartment() != null && member.getDepartment() >= 0) {
            Department dept = departmentRepository.findById(member.getDepartment()).orElse(null);
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println(member.getDepartment());
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println("=========================================================");
			System.out.println("=========================================================");
            if (dept != null) {
                // 로그인 후 AccountLogin 에서 문자로 변환
                accountLogin.setDepartment(dept.getDepartmentName()); 
            }
        }

        // 직급 이름 변환
        if (member.getPosition() != null && member.getPosition() > 0) {
            Position pos = positionRepository.findById(member.getPosition()).orElse(null);
            
            if (pos != null) {
                // 로그인 후 AccountLogin 에서 문자로 변환
                accountLogin.setPosition(pos.getPositionName());
            }
        }

        // 은행 이름 변환
        if (member.getBank() != null && member.getBank() > 0) {
            Bank bank = bankRepository.findById(member.getBank()).orElse(null);

            if (bank != null) {
                // 로그인 후 AccountLogin 에서 문자로 변환
                accountLogin.setBank(bank.getBankName());
            }
        }

        // 프로필 이미지 URL 설정
        if (member.getProfileImg() != null) {
            String url = member.getProfileImg().getImgLocation() + "/" + member.getProfileImg().getImgName();
            accountLogin.setProfileImgUrl(url);
        }

        return accountLogin;
    }
}
