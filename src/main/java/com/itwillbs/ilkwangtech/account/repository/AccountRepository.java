package com.itwillbs.ilkwangtech.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.member.entity.Member;

@Repository
public interface AccountRepository extends JpaRepository<Member, Long> {

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
