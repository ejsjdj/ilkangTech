package com.itwillbs.ilkwangtech.account.repository;

import java.util.List;
import java.util.Optional;

import com.itwillbs.ilkwangtech.account.entity.Departments;
import jakarta.validation.constraints.NotBlank;
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

	boolean existsByAccountNumber(@NotBlank(message = "계좌번호는 필수입니다") String accountNumber);


}
