package com.itwillbs.ilkwangtech.notice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.notice.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	// 고정 게시글 최대 3개 (최신순)
	List<Notice> findTop3ByIsPinnedTrueOrderByRegDateDesc();

	// 일반 게시글 페이징 (고정되지 않은 글들만 최신순으로)
	Page<Notice> findByIsPinnedFalseOrderByRegDateDesc(Pageable pageable);
	
	// 기존 코드에 추가
	long countByIsPinnedTrue();
	
	// 검색 기능이 포함된 페이징 쿼리
	@Query("SELECT n FROM Notice n " +
	           "WHERE n.isPinned = false " +
	           "AND (:startDate IS NULL OR n.regDate >= :startDate) " +
	           "AND (:endDate IS NULL OR n.regDate <= :endDate) " +
	           "AND (" +
	           "   :keyword IS NULL OR :keyword = '' OR " +
	           "   (:searchType = 'title' AND n.title LIKE %:keyword%) OR " +
	           "   (:searchType = 'content' AND n.content LIKE %:keyword%) OR " +
	           "   (:searchType = 'writer' AND n.writerName LIKE %:keyword%) " +
	           ") " +
	           "ORDER BY n.regDate DESC")
	    Page<Notice> searchNotices(@Param("startDate") LocalDateTime startDate,
	                               @Param("endDate") LocalDateTime endDate,
	                               @Param("searchType") String searchType,
	                               @Param("keyword") String keyword,
	                               Pageable pageable);

}
