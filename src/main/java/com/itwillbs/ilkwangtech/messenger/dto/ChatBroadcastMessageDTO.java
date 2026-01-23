package com.itwillbs.ilkwangtech.messenger.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatBroadcastMessageDTO {

    private Long roomId;
    private Long memberId;
    private String content;
    private String formattedTime;

}
