package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 사원 목록 조회를 위한 리포지토리
 */
public interface ListRepository extends JpaRepository<Member, Long> {

    /**
     * 이름, 사번, 전화번호, 이메일을 대상으로 키워드 검색을 수행합니다.
     *
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 사원 엔티티 Page
     */
    @Query("SELECT m FROM Member m WHERE " +
            "m.name LIKE %:keyword% OR " +
            "m.employeeNumber LIKE %:keyword% OR " +
            "m.phoneNumber LIKE %:keyword% OR " +
            "m.email LIKE %:keyword%")
    Page<Member> findBySearchKeyword(@Param("keyword") String keyword, Pageable pageable);

}
