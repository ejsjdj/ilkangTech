package com.itwillbs.ilkwangtech.messenger.controller;

import java.security.Principal;
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

    @MessageMapping("/chat.send")
    public void send(ChatSendRequestDTO req, Principal principal) {
        if (principal == null) return;

        // 1. 로그인 사용자 정보 추출
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        AccountLogin login = (AccountLogin) auth.getPrincipal();
        Long myId = login.getId();

        req.setMemberId(myId);
        req.setMsgType("TEXT");

        // 2. 메시지 저장
        ChatMessage saved = chatMessageService.saveTextMessage(req);
        
        // [중요] 메시지를 보낸 사람(나)은 지금 방을 보고 있으므로 즉시 읽음 처리
        messengerService.updateLastReadAt(saved.getRoomId(), myId);

        // 3. 브로드캐스트용 DTO 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN);
        ChatBroadcastMessageDTO out = new ChatBroadcastMessageDTO();
        out.setRoomId(saved.getRoomId());
        out.setMemberId(myId); // 누가 보냈는지 식별용
        out.setSenderName(login.getName());
        out.setContent(saved.getContent());
        out.setFormattedTime(saved.getCreatedAt().format(formatter));

        // 4. 채팅방 내부로 메시지 전송 (실시간 채팅창 업데이트)
        messagingTemplate.convertAndSend("/topic/chatroom/" + saved.getRoomId(), out);
        
        // 5. 모든 참여자의 개인 채널로 알림 신호 전송 (통합 로직)
        // DIRECT/GROUP 구분 없이 해당 방의 모든 멤버를 가져옵니다.
        List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(saved.getRoomId());
        for (ChatRoomMember m : members) {
            // m.getId().getMemberId()를 통해 각 참여자에게 신호를 쏩니다.
            messagingTemplate.convertAndSend("/topic/user/" + m.getId().getMemberId() + "/list", out);
        }
    }
}



