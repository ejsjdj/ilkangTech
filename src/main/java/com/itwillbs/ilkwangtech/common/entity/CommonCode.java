package com.itwillbs.ilkwangtech.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 공통코드 관리하는 엔티티
@Entity
// 테이블 제약조건 설정
// => 두 개의 컬럼 조합이 유니크인 조건 설정(그룹코드(group_code) 컬럼 + 공통코드(common_code) 컬럼 = 유니크이면 OK)
@Table(name = "common_code", uniqueConstraints = @UniqueConstraint(columnNames = {"group_code", "common_code"}))
@Getter
@Setter
@ToString
public class CommonCode {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 공통코드번호

    //	@Column(name = "group_code")
    @Column(nullable = true, length = 20)
    private String groupCode; // 상위공통코드(= 공통코드 그룹)

    @Column(name = "common_code", nullable = false, length = 50)
    private String commonCode; // 공통코드(하위 = 실제 사용하는 공통코드)

    @Column(name = "common_code_name", nullable = false, length = 20)
    private String commonCodeName; // 공통코드명(코드를 한국어로 표기할 컬럼)

    private String description; // 공통코드 상세설명
}