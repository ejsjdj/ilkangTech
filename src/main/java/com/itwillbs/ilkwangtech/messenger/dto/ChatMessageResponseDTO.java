package com.itwillbs.ilkwangtech.messenger.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResponseDTO {
    private Long memberId;
    private String memberName; 
    private String content;
    private LocalDateTime createdAt;
    private String msgType;
    private int unreadCount;
    private String formattedTime;
    private String deptName;
    private String positionName;
}