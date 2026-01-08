package com.itwillbs.ilkwangtech.account.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class AccountDTO {

    // 회원가입 요청을 받을 DTO
    public record AccountJoinRequest(

            // 사원사진(추후 구현)
            // 통장사본 첨부(추후 구현)

            @NotBlank(message = "이름은 필수입니다")
            @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다")
            String name,            // 이름

            @NotBlank(message = "성별은 필수입니다")
            @Pattern(regexp = "^(남|여)$", message = "성별은 '남' 또는 '여'이어야 합니다")
            String gender,          // 성별

            @NotNull(message = "입사일은 필수입니다")
            @PastOrPresent(message = "입사일은 현재 또는 과거여야 합니다")
            LocalDate joinDate,     // 입사일

            @NotBlank(message = "주민등록번호는 필수입니다")
            @Pattern(regexp = "^\\d{6}-\\d{7}$", message = "주민등록번호 형식이 올바르지 않습니다 (예: 123456-1234567)")
            String residentNumber,  // 주민등록번호

            @NotBlank(message = "이메일은 필수입니다")
            @Email(message = "올바른 이메일 형식이 아닙니다")
            String email,           // 이메일

            @NotBlank(message = "전화번호는 필수입니다")
            @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다 (예: 010-1234-5678)")
            String phoneNumber,     // 전화번호

            @NotBlank(message = "비밀번호는 필수입니다")
            @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
            String password,        // 비밀번호

            @NotBlank(message = "부서는 필수입니다")
            String department,      // 부서

            @NotBlank(message = "직급은 필수입니다")
            String position,        // 직급

            @NotBlank(message = "은행명은 필수입니다")
            String bank,            // 은행

            @NotBlank(message = "계좌번호는 필수입니다")
            @Pattern(regexp = "^[0-9]{10,20}$", message = "계좌번호는 10~20자의 숫자여야 합니다")
            String accountNumber,   // 계좌번호

            @NotBlank(message = "예금주는 필수입니다")
            @Size(min = 2, max = 50, message = "예금주는 2자 이상 50자 이하여야 합니다")
            String bankbookOwner    // 예금주
    ) {}

    // 회원가입 응답 DTO
    public record AccountJoinResponse(
            String employeeNumber   // 사원번호
    ) {}

    // 로그인 요청 DTO
    public record AccountLoginRequest (
            @NotBlank(message = "사원번호는 필수입니다")
            String employeeNumber, // 사원번호

            @NotBlank(message = "비밀번호는 필수입니다")
            String passWord        // 비밀번호
    ) {}

    // 로그인 응답 DTO
    public record AccountLoginResponse (
            String name,            // 이름
            String employeeNumber,  // 사원번호
            String gender,          // 성별
            LocalDate joinDate,     // 입사일
            String residentNumber,  // 주민등록번호
            String email,           // 이메일
            String phoneNumber,     // 전화번호
            String department,      // 부서
            String position,        // 직급
            String bank,            // 은행
            String accountNumber,   // 계좌번호
            String bankbookOwner,    // 예금주
            LocalDate lastLogin     // 마지막 로그인시간
    ) {}

}
