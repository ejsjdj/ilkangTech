package com.itwillbs.ilkwangtech.member.entity;

import com.itwillbs.ilkwangtech.common.entity.CommonCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 회원 권한을 관리하는 엔티티(= 공동코드 테이블의 상위공통코드명 "MEMBER_ROLE" 에 대한 공통코드 관리)
// => Member 엔티티와 CommonCode 엔티티는 다대다(N:M) 관계이므로
// 	  다대다 관계를 풀어서 중간 완충 역할을 수행할 MemberRole 테이블을 가운데에 끼워넣어 간단한 구조로 풀기
//    Member(1) : MemberRole(N) 관계
//	  CommonCode(1) : MemberRole(N) 관계
// ---------------------------------------------------------------------------------------------------------
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "member_role_seq_gen",
        sequenceName = "member_role_seq",
        initialValue = 200,
        allocationSize = 1
)
public class MemberRole {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_role_seq_gen")
    private Long id;

    // 사용자(Member) 엔티티와의 연관관계 설정
    // MemberRole(N) : Member(1) 이므로 @ManyToOne 어노테이션 지정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 공통코드(Commoncode) 엔티티와의 연관관계 설정
    // MemberRole(N) : CommonCode(1) 이므로 @ManyToOne 어노테이션 지정
    @ManyToOne(fetch = FetchType.LAZY)	// 지연로딩 설정(상대방이 현재 엔티티까지 무조건 조회하지 않고, 현재 엔티티에 접근하는 시점에 조회)
    @JoinColumn(name = "member_role_id", nullable = false) // common_code 테이블에 연결할 컬럼을 member_role 테이블의 member_role_id 컬럼으로 지정(FK 설정)
    private CommonCode role;

    // id 를 제외한 Member, CommonCode 엔티티를 전달받아 객체 초기화하는 파라미터 생성자 정의
    public MemberRole(Member member, CommonCode role) {
        this.member = member;
        this.role = role;
    }
}