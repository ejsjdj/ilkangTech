package com.itwillbs.ilkwangtech.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.member.entity.Member;

@Repository
public interface AccountRepository extends JpaRepository<Member, Long> {

    Member save(Member member);

    Optional<Member> findByEmployeeNumberAndPassword(String employeeNumber, String password);

	Optional<Member> findByEmployeeNumber(String employeeNumber);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByResidentNumber(String residentNumber);

}
