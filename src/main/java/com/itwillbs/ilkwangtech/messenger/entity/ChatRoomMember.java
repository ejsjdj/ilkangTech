package com.itwillbs.ilkwangtech.messenger.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chat_room_member")
@Getter @Setter @ToString
@NoArgsConstructor // 1. JPA 기본 생성자를 어노테이션으로 대체
public class ChatRoomMember {

	@EmbeddedId
    private ChatRoomMemberId id;

    @Column(name = "joined_at", nullable = false) // ★ joinedAt -> joined_at
    private LocalDateTime joinedAt;

    @Column(name = "left_at") // ★ leftAt -> left_at
    private LocalDateTime leftAt;

    @Column(name = "room_nickname", length = 100) // ★ 명시적으로 snake_case 지정
    private String roomNickname;
    
    @Column(name = "last_read_at") // ★ lastReadAt -> last_read_at
    private LocalDateTime lastReadAt;

    @PrePersist
    public void prePersist() {
        if (joinedAt == null) joinedAt = LocalDateTime.now();
        // 3. 참여 시점에 읽은 시간을 현재로 설정하여, 이전 메시지들이 '안 읽음'으로 뜨지 않게 합니다.
        if (lastReadAt == null) lastReadAt = LocalDateTime.now();
    }

    // 4. 생성 메서드를 하나로 통합 (필요 없는 중복 메서드 제거)
    public static ChatRoomMember of(Long roomId, Long memberId, String nickname) {
        ChatRoomMember m = new ChatRoomMember();
        m.setId(new ChatRoomMemberId(roomId, memberId));
        m.setRoomNickname(nickname);
        m.setLastReadAt(LocalDateTime.now()); // 명시적 초기화
        return m;
    }
}