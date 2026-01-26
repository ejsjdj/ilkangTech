package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMember;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberId;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {
	
	// 내가 참여 중인 그룹 채팅방의 ID 목록 가져오기
    List<ChatRoomMember> findByIdMemberId(Long memberId);
    
    // 특정 방에 속한 모든 멤버 ID 목록 가져오기
    List<ChatRoomMember> findByIdRoomId(Long roomId);
	
}
