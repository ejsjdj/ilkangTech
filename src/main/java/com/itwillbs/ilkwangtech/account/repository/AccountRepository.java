package com.itwillbs.ilkwangtech.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.member.entity.Member;

@Repository
public interface AccountRepository extends JpaRepository<Member, Long> {

	/*
	 * [ JPQL(Java Persistence Query Language) ]
	 * - JPA 에서 제공하는 객체지향 쿼리 언어
	 * - 기존 SQL 문처럼 DB 테이블을 대상으로 하는 것이 아니라, 엔티티 객체 및 필드를 대상으로 쿼리 작성
	 * - JPA 가 JPQL 을 해석하여 실제 DB 의 SQL 문장으로 변환하여 실행해준다!
	 * - 주의! 기본 문법 구조는 SQL 과 거의 동일하나, 테이블명 대신 엔티티명, 테이블의 컬럼명 대신 엔티티의 필드명 지정
	 */
	// Member 엔티티와 연관관계에 있는 MemberRole 에서 LAZY 로딩에 의한 로딩 문제를 해결하는 방법
	// email 을 기준으로 Member 엔티티와 함께 사용자 권한을 관리하는 MemberRole 엔티티도 함께 조회될 수 있도록 JPQL 을 사용하여 JOIN 구문 작성(JOIN FETCH 활용)
	// => 이 때, Member 엔티티의 roles 에 해당하는 MemberRole 엔티티의 CommonCode 엔티티(role)까지도 JOIN 해야함
	@Query("SELECT m FROM Member m"			// FROM 절 뒤에 테이블명 member 가 아닌 엔티티명 Member 로 지정
			+ " JOIN FETCH m.roles r"		// Member 엔티티의 roles 컬렉션에 해당하는 MemberRole 엔티티를 즉시 로딩(= EAGER)하여 가져오기 위한 JOIN(즉, JOIN FETCH 는 연관된 엔티티까지 한꺼번에 SELECT)
			+ " JOIN FETCH r.role"			// 중간 엔티티에 해당하는 MemberRole 내부의 CommonCode 엔티티를 다시 JOIN 해서 가져오기
			+ " WHERE m.employeeNumber = :employeeNumber")	// Member 엔티티의 employeeNumber(m.employeeNumber)이 메서드 파라미터로 전달된 email(:email)과 같은 조건 설정
	// 이 때, :employereNumber 로 지정한 email 파라미터를 JPQL 에서 접근하기 위해 @Param 어노테이션 적용하여 파라미터명 지정(org.springframework.data.repository.query.Param)
	Optional<Member> findByEmployeeNumberWithMemberRoles(@Param("employeeNumber") String employeeNumber);

	// 이메일 중복 확인
	boolean existsByEmail(String email);

	// 전화번호 중복 확인
	boolean existsByPhoneNumber(String phoneNumber);

	// 주민등록번호 중복 확인
	boolean existsByResidentNumber(String residentNumber);

	// 계좌번호 중복 확인
	boolean existsByAccountNumber(String accountNumber);

	// 직원번호 중복 확인
	boolean existsByEmployeeNumber(String employeeNumber);

	// 이메일로 조회
	Optional<Member> findByEmail(String email);

	Optional<Member> findByEmployeeNumber(String employeeNumber);
}
