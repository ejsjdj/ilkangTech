package com.itwillbs.ilkwangtech.messenger.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.messenger.dto.ChatBroadcastMessageDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatSendRequestDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoom;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMember;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomMemberRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomRepository;
import com.itwillbs.ilkwangtech.messenger.service.ChatMessageService;
import com.itwillbs.ilkwangtech.messenger.service.MessengerService;

@Controller
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final MessengerService messengerService;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatSocketController(SimpMessagingTemplate messagingTemplate,
                                ChatMessageService chatMessageService, ChatRoomRepository chatRoomRepository, ChatRoomMemberRepository chatRoomMemberRepository, MessengerService messengerService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
		this.messengerService = messengerService;
		this.chatRoomMemberRepository = chatRoomMemberRepository;
    }

 // ChatSocketController.java 내부의 send 함수 전체
    @MessageMapping("/chat.send")
    public void send(ChatSendRequestDTO req, Principal principal) {
        if (principal == null) return;
        
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        AccountLogin login = (AccountLogin) auth.getPrincipal();
        Long myId = login.getId();

        ChatBroadcastMessageDTO out = new ChatBroadcastMessageDTO();
        out.setRoomId(req.getRoomId());
        out.setMemberId(myId);
        out.setSenderName(login.getName());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN);

        // [수정 핵심] 파일/이미지 메시지는 이미 DB에 있으므로 브로드캐스트용 데이터만 세팅
        if ("IMAGE".equals(req.getMsgType()) || "FILE".equals(req.getMsgType())) {
            out.setContent(req.getContent());
            out.setMsgType(req.getMsgType());
            out.setAttachId(req.getAttachId()); // 전달받은 파일 ID 세팅
            out.setFormattedTime(LocalDateTime.now().format(formatter));
        } else {
            // 일반 텍스트일 때만 새로 저장
            req.setMemberId(myId);
            req.setMsgType("TEXT");
            ChatMessage saved = chatMessageService.saveTextMessage(req);
            
            out.setContent(saved.getContent());
            out.setMsgType("TEXT");
            out.setFormattedTime(saved.getCreatedAt().format(formatter));
        }

        // 채팅방 내부 및 목록 채널로 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + out.getRoomId(), out);
        
        // 알림 전송 (참여자 목록 순회)
        List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(out.getRoomId());
        for (ChatRoomMember m : members) {
            messagingTemplate.convertAndSend("/topic/user/" + m.getId().getMemberId() + "/list", out);
        }
    }
}



