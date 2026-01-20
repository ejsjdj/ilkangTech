package com.itwillbs.ilkwangtech.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	// 키워드로 특정 회원 정보를 조회하는 메서드
	List<Member> findByNameContaining(String keyword);

}
