package com.itwillbs.ilkwangtech.account.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    // ID
    private Long id;

    // 이름
    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다")
    private String name;

    // 사원번호
    @NotBlank(message = "사원번호는 필수입니다")
    @Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "사원번호는 6~10자의 영문대문자와 숫자 조합이어야 합니다")
    private String employeeNumber;

    // 성별
    @NotBlank(message = "성별은 필수입니다")
    @Pattern(regexp = "^(남|여)$", message = "성별은 남, 여 중 하나여야 합니다")
    private String gender;

    // 입사일
    @NotNull(message = "입사일은 필수입니다")
    @PastOrPresent(message = "입사일은 현재 날짜 이전이어야 합니다")
    private LocalDateTime joinDate;

    // 주민등록번호
    @NotBlank(message = "주민등록번호는 필수입니다")
    @Pattern(regexp = "^\\d{6}-[1-4]\\d{6}$", message = "주민등록번호 형식이 올바르지 않습니다 (예: 123456-1234567)")
    private String residentNumber;

    // 사원사진 (추후 구현)
    private String profilePhotoLink;

    // 이메일
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이어야 합니다")
    private String email;

    // 전화번호
    @NotBlank(message = "전화번호는 필수입니다")
    @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$|^\\d{2,3}-\\d{3,4}-\\d{4}$", 
             message = "전화번호 형식이 올바르지 않습니다 (예: 010-1234-5678 또는 02-123-4567)")
    private String phoneNumber;

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$", 
             message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다")
    private String password;

    // 부서
    @NotBlank(message = "부서는 필수입니다")
    @Size(min = 2, max = 50, message = "부서명은 2자 이상 50자 이하여야 합니다")
    private String department;

    // 직급
    @NotBlank(message = "직급은 필수입니다")
    @Size(min = 2, max = 30, message = "직급은 2자 이상 30자 이하여야 합니다")
    private String position;

    // 은행
    @NotBlank(message = "은행은 필수입니다")
    private String bank;

    // 계좌번호
    @NotBlank(message = "계좌번호는 필수입니다")
    private String accountNumber;

    // 통장사본 첨부 (추후 구현)
    private String accountPictureLink;

    // 마지막 로그인 시간
    @PastOrPresent(message = "마지막 로그인 시간은 현재 시간 이전이어야 합니다")
    private LocalDateTime lastLogin;

    // 수정 시간
    @PastOrPresent(message = "수정 시간은 현재 시간 이전이어야 합니다")
    private LocalDateTime updatedAt;
}
