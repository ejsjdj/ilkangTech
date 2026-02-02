package com.itwillbs.ilkwangtech.notice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.notice.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	// 고정 게시글 최대 3개 (최신순)
	List<Notice> findTop3ByIsPinnedTrueOrderByRegDateDesc();

	// 일반 게시글 페이징 (고정되지 않은 글들만 최신순으로)
	Page<Notice> findByIsPinnedFalseOrderByRegDateDesc(Pageable pageable);
	
	// 기존 코드에 추가
	long countByIsPinnedTrue();

}
