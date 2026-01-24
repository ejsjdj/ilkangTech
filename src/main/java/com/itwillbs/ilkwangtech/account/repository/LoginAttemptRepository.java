package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    Optional<LoginAttempt> findByMemberId(Long memberId);

}
