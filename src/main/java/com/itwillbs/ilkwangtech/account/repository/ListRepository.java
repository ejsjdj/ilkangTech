package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<Member, Long> {



}
