package com.itwillbs.ilkwangtech.messenger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    public ChatMessage saveTextMessage(Long roomId, Long memberId, String content) {

        ChatMessage msg = new ChatMessage();
        msg.setRoomId(roomId);
        msg.setMemberId(memberId);
        msg.setMsgType("TEXT");
        msg.setContent(content);

        return chatMessageRepository.save(msg);
    }
}
