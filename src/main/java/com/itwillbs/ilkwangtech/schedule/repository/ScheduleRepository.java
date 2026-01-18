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

	/*
     * [검색 조건]
     * 1. 권한: (일정 타입이 'COMPANY' 이거나) OR (작성자가 '나' 이거나)
     * 2. 날짜: 시작일이 검색 기간(start ~ end) 사이에 존재
     * 3. 검색어: 제목에 키워드 포함 (옵션)
     */
	@Query(value = "SELECT s FROM Schedule s " +
            "JOIN FETCH s.writer w " +
            "WHERE (s.type = 'COMPANY' OR w.id = :loginId) " +
            "AND (s.startDate BETWEEN :start AND :end) " +
            "AND (" +
            "   (:keyword IS NULL OR :keyword = '') OR " +
            "   (:searchType = 'title' AND s.title LIKE %:keyword%) OR " +
            "   (:searchType = 'content' AND s.content LIKE %:keyword%) OR " +
            "   (:searchType = 'writer' AND w.name LIKE %:keyword%) OR " +
            "   (:searchType = 'type' AND s.type LIKE %:keyword%) " + // [여기 추가됨]
            ")",
    countQuery = "SELECT count(s) FROM Schedule s " +
                 "JOIN s.writer w " +
                 "WHERE (s.type = 'COMPANY' OR w.id = :loginId) " +
                 "AND (s.startDate BETWEEN :start AND :end) " +
                 "AND (" +
                 "   (:keyword IS NULL OR :keyword = '') OR " +
                 "   (:searchType = 'title' AND s.title LIKE %:keyword%) OR " +
                 "   (:searchType = 'content' AND s.content LIKE %:keyword%) OR " +
                 "   (:searchType = 'writer' AND w.name LIKE %:keyword%) OR " +
                 "   (:searchType = 'type' AND s.type LIKE %:keyword%) " + // [여기 추가됨]
                 ")")
    Page<Schedule> findMyAndCompanySchedules(
         @Param("loginId") Long loginId,
         @Param("start") LocalDateTime start,
         @Param("end") LocalDateTime end,
         @Param("keyword") String keyword,
         @Param("searchType") String searchType,
         Pageable pageable
    );
	
}
