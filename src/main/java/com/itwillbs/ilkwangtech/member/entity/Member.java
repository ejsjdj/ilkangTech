package com.itwillbs.ilkwangtech.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 사용자 테이블을 관리하는 엔티티
@Entity
@Table(name = "members")
@Getter
@Setter
@ToString
@SequenceGenerator(
        name = "MEMBERS_SEQ_GENERATOR", // JPA 에서 사용할 시퀀스 이름(DB 의 시퀀스 이름이 아님!)
        sequenceName = "MEMBERS_SEQ",   // 오라클에서 사용할 시퀀스 이름
        initialValue = 10,				// 초기값(오라클 시퀀스의 start with 값과 동일)
        allocationSize = 1				// 증가값(오라클 시퀀스의 increment by 값과 동일)
)
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

    @Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "MEMBERS_SEQ_GENERATOR") // JPA 시퀀스 이름 지정
    private Long id;

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
    private LocalDate hireDate;

    // 주민등록번호
    @Column(length = 20)
    private String residentNumber;

    // 사원사진(추후 구현)
    private String profilePhotoLink;

    // 이메일
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    // 전화번호
    @Column(length = 20, nullable = false)
    private String phoneNumber;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 부서 (0 법인장 1 인사 2 구매 3 영업 4 재무회계 5 정보시스템 6 경영 7 안전 8 법무 100 공장장 101 프레스 102 사출 103 도장 104 조립 105 품질 106 금형 107 생산관리 108)
    @Column(length = 50)
    private Integer department;

    // 직급 (1 부장 2 차장 3 과장 4 대리 5 주임 6 사원 51 이사 52 상무이사 53 전무이사 54 부사장 55 사장 56 대표이사)
    //     (101 기능공 102 기능사 103 선임기능사 104 기능장 105 수석기능장)
    @Column(length = 50)
    private Integer position;

    // 은행 (4 국민은행 20 우리은행 88 신한은행 81 하나은행 11 농협은행 23 SC제일은행 27 시티은행)
    //     (90 카카오뱅크 92 토스뱅크)
    //     (48 신용협동조합 45 새마을금고 2 산업은행)
    @Column(length = 50)
    private Integer bank;

    // 계좌번호
    @Column(length = 30)
    private String accountNumber;

    // 통장사본 첨부(추후 구현)
    private String accountPictureLink;

    // 마지막 로그인 시간
    @Column
    private LocalDateTime lastLogin;

    // 수정 시간
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 사용자(Member)와 사용자권한(CommonCode)는 다대다(N:M) 관계이므로
    // 완충 작용을 담당하는 MemberRole 엔티티를 추가로 정의하여 각각의 관계를 1:N 으로 풀어냄(Member 와 MemberRole 은 1:N 관계)
    // => Member 엔티티에서 @OneToMany 어노테이션으로 1:N 관계에서의 1에 해당하는 엔티티 관계 지정
    // 1) mappedBy = "member" 속성 : 현재 엔티티가 연관관계의 주인이 아니며, 이 때 상대방의 필드명을 지정하여 해당 필드를 기준으로 매핑 수행
    // 2) cascade = CascadeType.ALL 속성 : 부모 엔티티가 저장/삭제될 경우 자식 엔티티도 저장/삭제
    // 3) orphanRemoval = true 속성 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티(= 고아객체) 자동으로 삭제
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRole> roles = new ArrayList<>();

}