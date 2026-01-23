package com.itwillbs.ilkwangtech.messenger.controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
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
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomRepository;
import com.itwillbs.ilkwangtech.messenger.service.ChatMessageService;

@Controller
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;

    public ChatSocketController(SimpMessagingTemplate messagingTemplate,
                                ChatMessageService chatMessageService, ChatRoomRepository chatRoomRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
		this.chatRoomRepository = chatRoomRepository;
    }

    @MessageMapping("/chat.send")
    public void send(ChatSendRequestDTO req, Principal principal) {

    	if (principal == null) {
            System.out.println("Principal이 null입니다. 인증 확인 필요!");
            return;
        }
        
    	// 로그인한 사용자의 id값 가져오기
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        AccountLogin login = (AccountLogin) auth.getPrincipal();

        Long myId = login.getId();

        req.setMemberId(myId);
        req.setMsgType("TEXT");

        ChatMessage saved = chatMessageService.saveTextMessage(req);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN);
        String timeStr = saved.getCreatedAt().format(formatter);

        ChatBroadcastMessageDTO out = new ChatBroadcastMessageDTO();
        out.setRoomId(saved.getRoomId());
        out.setMemberId(saved.getMemberId());
        out.setContent(saved.getContent());
        out.setFormattedTime(timeStr);

        messagingTemplate.convertAndSend("/topic/chatroom/" + saved.getRoomId(), out);
        
        // 2. 실시간 목록 갱신을 위해 참여자들에게 전송
        // 현재 방 정보를 가져와 참여자 ID를 확인합니다.
        ChatRoom room = chatRoomRepository.findById(saved.getRoomId()).orElse(null);
        if (room != null && "DIRECT".equals(room.getRoomType())) {
            messagingTemplate.convertAndSend("/topic/user/" + room.getDirectEmp1() + "/list", out);
            messagingTemplate.convertAndSend("/topic/user/" + room.getDirectEmp2() + "/list", out);
        }
    }
}


