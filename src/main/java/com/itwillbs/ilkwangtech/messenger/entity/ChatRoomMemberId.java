package com.itwillbs.ilkwangtech.messenger.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ChatRoomMemberId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "roomId")
    private Long roomId;

    @Column(name = "member_Id")
    private Long memberId;

    protected ChatRoomMemberId() {
    }

    public ChatRoomMemberId(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoomMemberId)) return false;
        ChatRoomMemberId that = (ChatRoomMemberId) o;
        return Objects.equals(roomId, that.roomId)
            && Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, memberId);
    }

}
