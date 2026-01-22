package com.itwillbs.ilkwangtech.messenger.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.messenger.dto.ChatBroadcastMessageDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatSendRequestDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.service.ChatMessageService;

@Controller
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatSocketController(SimpMessagingTemplate messagingTemplate,
                                ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat.send")
    public void send(ChatSendRequestDTO req,
                     @AuthenticationPrincipal AccountLogin login) {

        Long memberId = login.getId();

        // 1) DB 저장
        ChatMessage saved = chatMessageService.saveTextMessage(
                req.getRoomId(),
                memberId,
                req.getContent()
        );

        // 2) 브로커로 전송
        ChatBroadcastMessageDTO out = new ChatBroadcastMessageDTO();
        out.setRoomId(saved.getRoomId());
        out.setMemberId(saved.getMemberId());
        out.setContent(saved.getContent());

        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + saved.getRoomId(),
                out
        );
    }
}


