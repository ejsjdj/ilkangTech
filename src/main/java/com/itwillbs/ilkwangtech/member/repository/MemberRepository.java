package com.itwillbs.ilkwangtech.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	// 키워드로 특정 회원 정보를 조회하는 메서드
	List<Member> findByNameContaining(String keyword);

	// 부서 ID로 회원 목록을 조회하고 직급 순으로 정렬 (직급 ID가 낮을수록 높은 직급이라고 가정)
	List<Member> findByDepartmentOrderByPositionAsc(Integer departmentId);

}
