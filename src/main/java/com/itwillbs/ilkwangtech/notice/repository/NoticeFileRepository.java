package com.itwillbs.ilkwangtech.notice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.notice.entity.NoticeFile;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {
	
	// 특정 공지사항 게시글(noticeId)에 속한 모든 파일 리스트 조회
    // SELECT * FROM notice_file WHERE notice_id = ?
    List<NoticeFile> findByNoticeId(Long noticeId);
    
    // (선택 사항) 게시글 수정 시 기존 파일을 모두 삭제하고 새로 등록할 때 유용
    void deleteByNoticeId(Long noticeId);

}
