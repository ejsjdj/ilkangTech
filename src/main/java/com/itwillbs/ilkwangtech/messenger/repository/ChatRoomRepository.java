package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.messenger.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findByRoomTypeAndDirectEmp1AndDirectEmp2(String roomType, Long directEmp1, Long directEmp2);
}
