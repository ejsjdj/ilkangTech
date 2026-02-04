package com.itwillbs.ilkwangtech.messenger.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.messenger.entity.ChatAttachment;
import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoom;
import com.itwillbs.ilkwangtech.messenger.repository.ChatAttachmentRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatMessageRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatFileService {
    @Value("${file.uploadBaseLocation}")
    private String uploadBaseLocation;
    @Value("${file.chatFileLocation}")
    private String chatFileLocation;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatAttachmentRepository chatAttachmentRepository;
    
    public ChatFileService(ChatAttachmentRepository attachmentRepository, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, MemberRepository memberRepository, ChatAttachmentRepository chatAttachmentRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.memberRepository = memberRepository;
		this.chatAttachmentRepository = chatAttachmentRepository;
    }

 // ChatFileService.java 내부의 saveChatFile 함수 전체
    @Transactional
    public ChatAttachment saveChatFile(MultipartFile mFile, Long roomId, Long memberId) throws IOException {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        // 1. 이미지 여부 판별
        String contentType = mFile.getContentType();
        String msgType = (contentType != null && contentType.startsWith("image")) ? "IMAGE" : "FILE";

        // 2. 파일용 메시지를 먼저 생성 (타입을 정확히 지정)
        ChatMessage message = new ChatMessage();
        message.setRoomId(roomId);
        message.setMemberId(memberId);
        message.setContent("(파일 첨부: " + mFile.getOriginalFilename() + ")");
        message.setMsgType(msgType); // IMAGE 또는 FILE로 저장
        chatMessageRepository.save(message);

        // 3. 파일 물리 저장
        String subDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path uploadDir = Paths.get(uploadBaseLocation, chatFileLocation, subDir).toAbsolutePath().normalize();
        if(!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

        String originalName = mFile.getOriginalFilename();
        String storedName = UUID.randomUUID().toString() + "_" + originalName;
        mFile.transferTo(uploadDir.resolve(storedName));

        // 4. 첨부파일 정보 저장
        ChatAttachment attach = new ChatAttachment();
        attach.setMessage(message);
        attach.setRoom(room);
        attach.setMember(member);
        attach.setOriginalName(originalName);
        attach.setStoredName(storedName);
        attach.setStorePath(chatFileLocation + "/" + subDir);
        attach.setMimeType(contentType);
        attach.setFileSize(mFile.getSize());
        attach.setIsImage(msgType.equals("IMAGE") ? "Y" : "N");

        return chatAttachmentRepository.save(attach);
    }
}
