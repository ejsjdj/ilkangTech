package com.itwillbs.ilkwangtech.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "members")
public class Member {

    // Member 클래스에 필요한 필드
    // 이름
    // 사원번호
    // 성별
    // 입사일
    // 주민등록번호
    // 사원사진(추후 구현)
    // 이메일
    // 전화번호
    // 비밀번호
    // 부서
    // 직급
    // 급여통장
    // 계좌번호
    // 예금주
    // 통장사본 첨부(추후 구현)
    // 마지막 로그인 시간

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;         // ID

    // 이름
    @Column(length = 50, nullable = false)
    private String name;

    // 사원번호
    @Column(length = 20, nullable = false, unique = true)
    private String employeeNumber;

    // 성별 (1은 남자 2는 여자)
    @Column(length = 10, nullable = false)
    private int gender;

    // 입사일
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime joinDate;

    // 주민등록번호
    @Column(length = 20)
    @Pattern(regexp = "^\\d{6}-\\d{7}$")
    private String residentNumber;

    // 사원사진(추후 구현)
    private String profilePhotoLink;

    // 이메일
    @Column(length = 100, nullable = false, unique = true)
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    // 전화번호
    @Column(length = 20, nullable = false)
    @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$")
    @NotBlank(message = "전화번호는 필수입니다")
    private String phoneNumber;

    // 비밀번호
    @Column(length = 255, nullable = false)
    private String password;

    // 부서 (0 법인장 1 인사 2 구매 3 영업 4 재무회계 5 정보시스템 6 경영 7 안전 8 법무 100 공장장 101 프레스 102 사출 103 도장 104 조립 105 품질 106 금형 107 생산관리 108)
    @Column(length = 50)
    private int department;

    // 직급 (1 부장 2 차장 3 과장 4 대리 5 주임 6 사원 51 이사 52 상무이사 53 전무이사 54 부사장 55 사장 56 대표이사)
    //     (101 기능공 102 기능사 103 선임기능사 104 기능장 105 수석기능장)
    @Column(length = 50)
    private int position;

    // 은행 (4 국민은행 20 우리은행 88 신한은행 81 하나은행 11 농협은행 23 SC제일은행 27 시티은행)
    //     (90 카카오뱅크 92 토스뱅크)
    //     (48 신용협동조합 45 새마을금고 2 산업은행)
    @Column(length = 50)
    private int bank;

    // 계좌번호
    @Column(length = 30)
    private String accountNumber;

    // 통장사본 첨부(추후 구현)
    private String accountPictureLink;

    // 마지막 로그인 시간
    @Column
    private LocalDateTime lastLogin;

    // 수정 시간
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}