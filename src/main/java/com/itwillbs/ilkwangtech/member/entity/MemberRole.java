package com.itwillbs.ilkwangtech.member.entity;

import com.itwillbs.ilkwangtech.common.entity.CommonCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원(Member)과 권한(CommonCode) 사이의 매핑을 담당하는 엔티티
 * 다대다 관계를 일대다-다대일 관계로 풀어낸 중간 테이블 역할을 합니다.
 */
@Entity
@Table(name = "member_role")
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

    /**
     * 권한을 부여받은 사원 정보 (N:1)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 사원에게 부여된 권한 정보 (N:1)
     * 공통코드 테이블의 MEMBER_ROLE 그룹 코드를 참조합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_role_id", nullable = false)
    private CommonCode role;

    /**
     * 회원과 권한 정보를 받아 객체를 생성하는 생성자
     */
    public MemberRole(Member member, CommonCode role) {
        this.member = member;
        this.role = role;
    }
}