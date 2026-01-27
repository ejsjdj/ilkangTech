package com.itwillbs.ilkwangtech.messenger.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chatRoomMember")
@Getter
@Setter
@ToString
public class ChatRoomMember {

    @EmbeddedId
    private ChatRoomMemberId id;

    @Column(name = "joinedAt", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "leftAt")
    private LocalDateTime leftAt;

    protected ChatRoomMember() {
        // JPA 기본 생성자
    }

    public ChatRoomMemberId getId() {
        return id;
    }

    public void setId(ChatRoomMemberId id) {
        this.id = id;
    }

    @PrePersist
    public void prePersist() {
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
    }

    public static ChatRoomMember of(Long roomId, Long memberId) {
        ChatRoomMember m = new ChatRoomMember();
        m.setId(new ChatRoomMemberId(roomId, memberId));
        return m;
    }
}


