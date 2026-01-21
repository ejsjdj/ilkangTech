package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ListRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE " +
            "m.name LIKE %:keyword% OR " +
            "m.employeeNumber LIKE %:keyword% OR " +
            "m.phoneNumber LIKE %:keyword% OR " +
            "m.email LIKE %:keyword%")
    Page<Member> findBySearchKeyword(@Param("keyword") String keyword, Pageable pageable);

}
