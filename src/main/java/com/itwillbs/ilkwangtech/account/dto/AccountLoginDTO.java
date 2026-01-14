package com.itwillbs.ilkwangtech.account.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.itwillbs.ilkwangtech.member.entity.MemberRole;

public class AccountLoginDTO implements UserDetails {
	
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
 		// return roles; // 오류 발생! (Type mismatch: cannot convert from List<MemberRole> to Collection<? extends GrantedAuthority>)
 		// => 주의! 사용자 권한 목록을 저장하는 List<MemberRole> 타입은 개발자가 사용하는 컬렉션 타입이며
 		//    스프링 시큐리티가 관리하는 권한 목록은 Collection<? extends GrantedAuthority> 타입 객체가 리턴되어야 함
 		// -----------------------------
 		// 컬렉션(List, Set 등)을 또 다른 컬렉션 형태로 변환
 		return roles.stream() // 컬렉션 요소를 자바 스트림 형태로 변환(Stream<MemberRole> 타입으로 변환)
 				.map(role -> new SimpleGrantedAuthority(role.getRole().getCommonCode())) // 스트림의 각 요소(각각의 MemberRole 객체)를 다른 객체(스프링 시큐리티가 관리하는 권한 객체(SimpleGrantedAuthority)) 형태로 변환
 				// => 이 때, 권한 목록을 각각 분리(roles -> role)해서 각 권한에 대한 공통코드(common_code 컬럼값)만 꺼내서 변환
 				.collect(Collectors.toList()); // 권한 정보 1개를 갖는 SimpleGrantedAuthority 객체들을 List 객체로 모아서 리턴
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
