package com.itwillbs.ilkwangtech.messenger.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	
	List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);
	
	// 특정 방의 가장 최근 메시지 하나만 가져오기
    Optional<ChatMessage> findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);
    
    // 특정 시점 이후의 모든 메세지 조회
    @Query("SELECT m FROM ChatMessage m WHERE m.roomId = :roomId AND m.createdAt > :joinedAt ORDER BY m.createdAt ASC")
    List<ChatMessage> findHistory(Long roomId, LocalDateTime joinedAt);

    // 목록용: 특정 시각 이후의 최신 메시지 하나만 조회
    @Query("SELECT m FROM ChatMessage m WHERE m.roomId = :roomId AND m.createdAt > :joinedAt ORDER BY m.createdAt DESC")
    Optional<ChatMessage> findLatest(Long roomId, LocalDateTime joinedAt);
}
