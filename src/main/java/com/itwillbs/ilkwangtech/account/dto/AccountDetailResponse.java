package com.itwillbs.ilkwangtech.account.dto;

import com.itwillbs.ilkwangtech.common.entity.CommonCode;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.entity.MemberRole;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 사원 상세 정보 조회 시 사용하는 응답 DTO
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDetailResponse {
    private Long id;                // 고유 ID
    private String employeeNumber;  // 사원번호
    private String name;            // 이름
    private String gender;             // 성별
    private LocalDate hireDate;     // 입사일
    private String residentNumber;  // 주민등록번호
    private String email;           // 이메일
    private String phoneNumber;     // 전화번호
    private String department;      // 부서명
    private String position;        // 직급명
    private String bank;            // 은행명
    private String accountNumber;   // 계좌번호
    private String profileImgUrl; // 프로필 사진 경로
    private List<String> roles;     // 보유 권한 목록 (권한명 리스트)
    private String status;          // 재직 상태

    public static AccountDetailResponse of(Member member, String department, String position, String bank, String profileImgUrl) {
        return AccountDetailResponse.builder()
                .id(member.getId())
                .employeeNumber(member.getEmployeeNumber())
                .accountNumber(member.getAccountNumber())
                .name(member.getName())
                .gender(member.getGender() == 1 ? "남" : "여")
                .hireDate(member.getHireDate())
                .residentNumber(member.getResidentNumber())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .department(department)
                .position(position)
                .bank(bank)
                .profileImgUrl(profileImgUrl)
                .roles(member.getRoles().stream()
                        .map(MemberRole::getRole)
                        .map(CommonCode::getCommonCodeName)
                        .toList())
                .build();
    }
}