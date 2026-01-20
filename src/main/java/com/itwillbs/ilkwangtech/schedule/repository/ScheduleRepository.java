package com.itwillbs.ilkwangtech.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.schedule.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	
	@Query("SELECT s FROM Schedule s " +
	           "LEFT JOIN s.writer w " + // 작성자 이름 검색을 위해 writer는 조인 유지
	           "WHERE " +
	           " ( " +
	           "   s.writer.id = :memberId " +                             // 1. 내가 쓴 글
	           "   OR s.type = 'COMPANY' " +                               // 2. 회사 전체
	           // [핵심 변경] JOIN 대신 EXISTS 사용 (중복 제거 효과)
	           "   OR (s.type = 'TEAM' AND EXISTS (SELECT 1 FROM s.sharedDepartments sd WHERE sd.id = :deptId)) " + 
	           "   OR (s.type = 'SPECIFIC' AND EXISTS (SELECT 1 FROM s.sharedMembers sm WHERE sm.id = :memberId)) " +
	           " ) " +
	           " AND (s.startDate BETWEEN :start AND :end OR s.endDate BETWEEN :start AND :end) " + // 날짜 범위
	           " AND ( " + // 검색 조건
	           "   (:keyword IS NULL OR :keyword = '') " +
	           "   OR (:searchType = 'title' AND s.title LIKE %:keyword%) " +
	           "   OR (:searchType = 'content' AND s.content LIKE %:keyword%) " +
	           "   OR (:searchType = 'writer' AND w.name LIKE %:keyword%) " +
	           "   OR (:searchType = 'type' AND s.type LIKE %:keyword%) " +
	           " )")
	    Page<Schedule> findWithSharing(
	            @Param("memberId") Long memberId,
	            @Param("deptId") int deptId,
	            @Param("start") LocalDateTime start,
	            @Param("end") LocalDateTime end,
	            @Param("keyword") String keyword,
	            @Param("searchType") String searchType,
	            Pageable pageable
	    );
	
	
	
}
