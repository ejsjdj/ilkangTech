package com.itwillbs.ilkwangtech.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
