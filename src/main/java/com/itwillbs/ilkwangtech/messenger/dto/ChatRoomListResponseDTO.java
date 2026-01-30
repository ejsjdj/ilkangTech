package com.itwillbs.ilkwangtech.messenger.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatRoomListResponseDTO {
    private Long roomId;
    private String roomTitle;    // 상대방 이름 또는 그룹방 이름
    private String lastMessage;  // 마지막 메시지 내용
    private String lastTime;     // 마지막 메시지 시간 (ex) 오후 1:32)
    private LocalDateTime lastMessageAt;
    private String isFavorite = "N";
}
