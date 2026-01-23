package com.itwillbs.ilkwangtech.messenger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.messenger.dto.ChatSendRequestDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    public ChatMessage saveTextMessage(ChatSendRequestDTO dto) { // 1. void를 ChatMessage로 변경
    	System.out.println("서비스 수신 memberId : " + dto.getMemberId());
        ChatMessage message = new ChatMessage();
        message.setRoomId(dto.getRoomId());
        message.setMemberId(dto.getMemberId());
        message.setContent(dto.getContent());
        message.setMsgType(dto.getMsgType());
        
        return chatMessageRepository.save(message); 
    }
}
