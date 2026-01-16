package com.itwillbs.ilkwangtech.account.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.itwillbs.ilkwangtech.member.entity.MemberRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AccountLogin implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	Long id;					// 아이디
    String name;            	// 이름
    String employeeNumber;  	// 사원번호
    String password;			// 비밀번호
    int gender;          	// 성별
    LocalDateTime joinDate;     // 입사일
    String residentNumber;  	// 주민등록번호
    String email;           	// 이메일
    String phoneNumber;     	// 전화번호
    int department;      	// 부서
    int position;        	// 직급
    int bank;            	// 은행
    String accountNumber;   	// 계좌번호
    LocalDateTime lastLogin;	// 마지막 로그인시간
    
    List<MemberRole> roles; 	// 사용자 권한 목록
    
    
    
    // ----------------------------------------------------------
 	// 필수 오버라이딩 메서드
 	// 1) 사용자의 권한 목록 리턴하는 메서드
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 임시 해결: 권한이 없으면 기본 권한 부여
		if (roles == null || roles.isEmpty()) {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		}

		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole().getCommonCode()))
				.collect(Collectors.toList());
	}

 	// 2) 사용자명(= 아이디 역할)을 리턴하는 메서드
 	// => 현재 사용자명을 이메일로 대체하여 사용하므로 이메일 주소값 리턴
 	@Override
 	public String getUsername() {
 		return this.employeeNumber;
 	}
 	
 	// 3) 사용자 패스워드를 리턴하는 메서드
 	@Override
 	public String getPassword() {
 		return this.password;
 	}

 	// ----------------------------------------------------------
 	// 선택적 오버라이딩 메서드
 	// 4) 계정 만료 여부 리턴
 	@Override
 	public boolean isAccountNonExpired() {
 		// 실제 계정 만료 여부 확인하는 서비스 로직 추가 필요
 		// => ex) memberRepository.isAccountNonExpired() 등의 메서드로 조회
 		return true; // 만료되지 않았다는 의미로 임의의 값 true 리턴
 	}

 	// 5) 계정 잠금 여부 리턴
 	@Override
 	public boolean isAccountNonLocked() {
 		// 실제 계정 잠금 여부 확인하는 서비스 로직 추가 필요
 		return true; // 잠기지 않았다는 의미로 임의의 값 true 리턴
 	}

 	// 6) 인증 기간 만료(패스워드 기간 만료) 여부 리턴
 	@Override
 	public boolean isCredentialsNonExpired() {
 		// 실제 패스워드 기간 만료 여부 확인하는 서비스 로직 추가 필요
 		return true; // 만료되지 않았다는 의미로 임의의 값 true 리턴
 	}

 	// 7) 계정 사용 가능(활성화) 여부 리턴
 	@Override
 	public boolean isEnabled() {
 		// 실제 계정 활성화 여부 확인하는 서비스 로직 추가 필요
 		return true; // 활성화 상태라는 의미로 임의의 값 true 리턴
 	}
}
