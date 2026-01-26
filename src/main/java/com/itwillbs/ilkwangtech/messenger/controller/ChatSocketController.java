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

@Controller
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatSocketController(SimpMessagingTemplate messagingTemplate,
                                ChatMessageService chatMessageService, ChatRoomRepository chatRoomRepository, ChatRoomMemberRepository chatRoomMemberRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoomMemberRepository = chatRoomMemberRepository;
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
        out.setSenderName(login.getName());
        out.setContent(saved.getContent());
        out.setFormattedTime(timeStr);

        // 채팅방 토픽으로 메시지 전송 (모든 구독자)
        messagingTemplate.convertAndSend("/topic/chatroom/" + saved.getRoomId(), out);
        
        // 디버깅을 위한 로그 추가
        // 임시
        System.out.println("메시지 전송 - 방 ID: " + saved.getRoomId() + 
                          ", 보낸 사람: " + saved.getMemberId() + 
                          ", 내용: " + saved.getContent());
        
        // 2. 실시간 목록 갱신을 위해 참여자들에게 전송
        // 현재 방 정보를 가져와 참여자 ID를 확인합니다.
        ChatRoom room = chatRoomRepository.findById(saved.getRoomId()).orElse(null);
        if (room != null) {
            if ("DIRECT".equals(room.getRoomType())) {
                // 1:1 채팅인 경우 (기존 로직)
                messagingTemplate.convertAndSend("/topic/user/" + room.getDirectEmp1() + "/list", out);
                messagingTemplate.convertAndSend("/topic/user/" + room.getDirectEmp2() + "/list", out);
            } else {
                // ★ 그룹 채팅인 경우: 해당 방의 모든 멤버를 찾아서 전송 ★
                List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(room.getId());
                for (ChatRoomMember m : members) {
                    messagingTemplate.convertAndSend("/topic/user/" + m.getId().getMemberId() + "/list", out);
                }
            }
        }
    }
}



