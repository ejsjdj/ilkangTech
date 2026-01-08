package com.itwillbs.ilkwangtech.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
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

    // 성별
    @Column(length = 10, nullable = false)
    private String gender;

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

    // 부서
    @Column(length = 50)
    private String department;

    // 직급
    @Column(length = 50)
    private String position;

    // 은행
    @Column(length = 50)
    private String bank;

    // 계좌번호
    @Column(length = 30)
    @Pattern(regexp = "^\\d{10,16}$")
    private String accountNumber;

    // 예금주
    @Column(length = 50)
    private String accountOwner;

    // 통장사본 첨부(추후 구현)
    private String accountPictureLink;

    // 마지막 로그인 시간
    @Column
    private LocalDateTime lastLogin;

    // 수정 시간
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}