package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	
	List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);
	
	// 특정 방의 가장 최근 메시지 하나만 가져오기
    Optional<ChatMessage> findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);
}
