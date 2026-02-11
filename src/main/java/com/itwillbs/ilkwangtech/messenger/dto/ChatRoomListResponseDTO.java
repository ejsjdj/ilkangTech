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
    private String roomTitle;    
    private String lastMessage;  
    private String lastTime;     
    private LocalDateTime lastMessageAt;
    private String isFavorite = "N";
    private int unreadCount;
}
