package com.itwillbs.ilkwangtech.messenger.entity;

import java.time.LocalDateTime;

import com.itwillbs.ilkwangtech.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_room_member_setting")
@Getter @Setter @NoArgsConstructor
public class ChatRoomMemberSetting {
	
    @EmbeddedId
    private ChatRoomMemberSettingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @Column(name = "display_room_name", length = 100)
    private String displayRoomName;

    @Column(name = "is_favorite", nullable = false, length = 1)
    private String isFavorite = "N";

    @Column(name = "favorited_at")
    private LocalDateTime favoritedAt;
}
