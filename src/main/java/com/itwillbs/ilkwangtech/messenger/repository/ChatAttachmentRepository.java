package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.messenger.entity.ChatAttachment;

public interface ChatAttachmentRepository extends JpaRepository<ChatAttachment, Long> {

	// 특정 메시지에 첨부된 파일 정보를 찾기 위한 메서드
	Optional<ChatAttachment> findByMessageId(Long msgId);

    // 저장된 파일명(UUID 포함)으로 정보를 찾을 때 사용 (Unique 제약조건 활용)
	Optional<ChatAttachment> findByStoredName(String storedName);
}
