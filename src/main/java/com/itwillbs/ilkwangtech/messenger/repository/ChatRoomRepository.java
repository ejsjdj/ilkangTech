package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.messenger.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	
	Optional<ChatRoom> findByRoomTypeAndDirectEmp1AndDirectEmp2(String roomType, Long directEmp1, Long directEmp2);
	
	// 1:1 채팅방 찾기 (내가 emp1이거나 emp2인 경우)
    List<ChatRoom> findByDirectEmp1OrDirectEmp2(Long emp1, Long emp2);
}
