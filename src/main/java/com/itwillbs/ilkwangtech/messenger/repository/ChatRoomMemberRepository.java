package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMember;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberId;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {
    
    // 내 ID로 참여 중인 모든 정보 가져오기
    @Query("SELECT cm FROM ChatRoomMember cm WHERE cm.id.memberId = :memberId")
    List<ChatRoomMember> findByMemberId(@Param("memberId") Long memberId);

    // 특정 방에 속한 모든 멤버 정보 가져오기
    @Query("SELECT cm FROM ChatRoomMember cm WHERE cm.id.roomId = :roomId")
    List<ChatRoomMember> findByRoomId(@Param("roomId") Long roomId);
}