package com.itwillbs.ilkwangtech.account.dto;

import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import com.itwillbs.ilkwangtech.account.entity.ProfileImg;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.entity.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security의 UserDetails를 구현한 클래스
 * 인증된 사용자의 정보와 권한을 담는 객체로 세션에 저장됩니다.
 */
@Getter
@Builder
@ToString
public class AccountLogin implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;					// 회원 고유 ID
	private String name;            	// 이름
	private String employeeNumber;  	// 사원번호 (로그인 ID로 사용)
	private String password;			// 암호화된 비밀번호게
	private int gender;          		// 성별
	private LocalDate hireDate;     // 입사일
	private String residentNumber;  	// 주민등록번호

	@Email
	private String email;           	// 이메일

	@Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private String phoneNumber;     	// 전화번호

	private String department;      	// 부서명
	private String position;        	// 직급명
	private String bank;            	// 은행명
	private String accountNumber;   	// 계좌번호
	private List<ProfileImg> profileImgs;   	// 프로필 이미지 URL
	private LocalDateTime lastLogin;	// 마지막 로그인 시간

	private List<MemberRole> roles; 	// 사용자가 보유한 권한 목록

	private LoginAttempt loginAttempt;  // 로그인 시도 및 잠금 정보
    // ----------------------------------------------------------
 	/**
 	 * 사용자의 권한 목록을 GrantedAuthority 객체 컬렉션으로 리턴합니다.
 	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 보유한 권한이 없는 경우 기본적으로 ROLE_USER 권한 부여
		if (roles == null || roles.isEmpty()) {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		}

		// MemberRole 엔티티 목록을 Security 권한 객체로 변환
		return roles.stream()
				.map(role -> {
					String roleName = role.getRole().getCommonCode();
					// DB의 권한 코드(예: ROLE_ADMIN)를 그대로 사용
					return new SimpleGrantedAuthority(roleName);
				})
				.collect(Collectors.toList());
	}

 	/**
 	 * 사용자 식별값(Username)을 리턴합니다.
 	 * 본 시스템에서는 사원번호를 로그인 ID로 사용합니다.
 	 */
 	@Override
 	public String getUsername() {
 		return this.employeeNumber;
 	}

 	/**
 	 * 암호화된 비밀번호를 리턴합니다.
 	 */
 	@Override
 	public String getPassword() {
 		return this.password;
 	}

 	// ----------------------------------------------------------
 	/**
 	 * 계정 만료 여부를 리턴합니다. (true: 만료되지 않음)
 	 */
 	@Override
 	public boolean isAccountNonExpired() {
 		return true;
 	}

 	/**
 	 * 계정 잠금 여부를 리턴합니다. (true: 잠기지 않음)
 	 * LoginAttempt 정보를 확인하여 잠금 여부를 판단합니다.
 	 */
 	@Override
 	public boolean isAccountNonLocked() {
		if (loginAttempt == null) return true;
		else if (loginAttempt.isLocked()) return false;
		return true;
 	}

 	/**
 	 * 패스워드 만료 여부를 리턴합니다. (true: 만료되지 않음)
 	 */
 	@Override
 	public boolean isCredentialsNonExpired() {
 		return true;
 	}

 	/**
 	 * 계정 활성화 여부를 리턴합니다. (true: 활성화)
 	 */
 	@Override
 	public boolean isEnabled() {
		return true;
 	}

	 // 프로필 이미지를 재설정
	 public void updateSortImages(List<ProfileImg> sortedImgs) {
		 this.profileImgs = sortedImgs;
	 }

	 // 빌더
	// AccountLogin 클래스 내부의 of 메서드 수정
	 public static AccountLogin of(Member member, String department, String position, String bankName) {
		 return AccountLogin.builder()
				 .id(member.getId()) // ID가 있어야 수정 폼에서 pk를 인식합니다
				 .employeeNumber(member.getEmployeeNumber())
				 .password(member.getPassword())
				 .name(member.getName()) // member에서 직접 가져오기
				 .department(department)
				 .position(position)
				 .bank(bankName)
				 .hireDate(member.getHireDate())
				 .gender(member.getGender())
				 .residentNumber(member.getResidentNumber())
				 .email(member.getEmail())
				 .phoneNumber(member.getPhoneNumber())
				 .accountNumber(member.getAccountNumber())
				 .profileImgs(member.getProfileImgs())
				 .roles(member.getRoles())
				 .build();
	 }

	 // email, phoneNumber 세터
	 public boolean updateInfo(String email, String phoneNumber) {
		 boolean isEmailValid = (email != null && email.contains("@"));
		 boolean isPhoneValid = (phoneNumber != null && phoneNumber.matches("^\\d{3}-\\d{3,4}-\\d{4}$"));

		 if (!(isEmailValid && isPhoneValid)) return false;

		 this.email = email;
		 this.phoneNumber = phoneNumber;

		 return true;
	 }
}
