package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberSetting;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberSettingId;

public interface ChatRoomSettingRepository extends JpaRepository<ChatRoomMemberSetting, ChatRoomMemberSettingId> {

	@Query("SELECT s FROM ChatRoomMemberSetting s WHERE s.id.memberId = :myId AND s.displayRoomName = :targetName")
	Optional<ChatRoomMemberSetting> findByMemberIdAndRoomName(@Param("myId") Long myId, @Param("targetName") String targetName);
	
}
