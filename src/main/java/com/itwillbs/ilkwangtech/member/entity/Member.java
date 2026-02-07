package com.itwillbs.ilkwangtech.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itwillbs.ilkwangtech.account.constant.MemberStatus;
import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import com.itwillbs.ilkwangtech.account.entity.ProfileImg;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 사원 정보를 관리하는 엔티티 클래스
 * DB의 members 테이블과 매핑됩니다.
 */
@Entity
@Table(name = "members")
@Getter
@Setter
@ToString
@SequenceGenerator(
        name = "MEMBERS_SEQ_GENERATOR", 
        sequenceName = "MEMBERS_SEQ",   
        initialValue = 1000,
        allocationSize = 1				
)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBERS_SEQ_GENERATOR") 
    private Long id; // 고유 식별자

    @Column(length = 50, nullable = false)
    private String name; // 이름

    @Column(length = 20, nullable = false, unique = true)
    private String employeeNumber; // 사원번호 (로그인 ID)

    @Column(length = 10, nullable = false)
    private int gender; // 성별 (1: 남자, 2: 여자)

    @Column(nullable = false)
    private LocalDate hireDate; // 입사일

    @Column(length = 20)
    private String residentNumber; // 주민등록번호

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "member")
    private List<ProfileImg> profileImgs;

    @Column(length = 100, nullable = false, unique = true)
    private String email; // 이메일

    @Column(length = 20, nullable = false)
    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(length = 50)
    private Integer department; // 부서 코드 (Common Code ID)

    @Column(length = 50)
    private Integer position; // 직급 코드 (Common Code ID)

    @Column(length = 50)
    private Integer bank; // 은행 코드 (Common Code ID)

    @Column(length = 30)
    private String accountNumber; // 계좌번호

    private String accountPictureLink; // 통장사본 이미지 경로

    @Column
    private LocalDateTime lastLogin; // 마지막 로그인 일시

    @LastModifiedDate
    private LocalDateTime updatedAt; // 정보 수정 일시

    @Column(length = 20, nullable = true)
    private MemberStatus status;

    public void changeStatus(MemberStatus status) {
        this.status = status;
    }

    /**
     * 사원이 보유한 권한 목록 (1:N 관계)
     * MemberRole 엔티티를 통해 권한(CommonCode)과 다대다 관계를 맺습니다.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRole> roles = new ArrayList<>();

    /**
     * 로그인 시도 및 계정 잠금 정보 (1:1 관계)
     */
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LoginAttempt loginAttempt;

}