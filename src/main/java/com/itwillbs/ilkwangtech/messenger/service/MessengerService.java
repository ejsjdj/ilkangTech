package com.itwillbs.ilkwangtech.messenger.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.messenger.dto.ChatBroadcastMessageDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatMessageResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatRoomListResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.MemberDeptRowDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoom;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMember;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberId;
import com.itwillbs.ilkwangtech.messenger.repository.ChatMessageRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomMemberRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomRepository;
import com.itwillbs.ilkwangtech.messenger.repository.MessengerRepository;

@Service
public class MessengerService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MessengerRepository messengerRepository;

    public MessengerService(ChatRoomRepository chatRoomRepository,
                            ChatRoomMemberRepository chatRoomMemberRepository, MessengerRepository messengerRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.messengerRepository = messengerRepository;
    }

    @Transactional
    public Long getOrCreateDirectRoom(Long myMemberId, Long targetMemberId) {

        if (myMemberId == null || targetMemberId == null) {
            throw new IllegalArgumentException("memberId는 null일 수 없습니다.");
        }
        if (myMemberId.equals(targetMemberId)) {
            throw new IllegalArgumentException("자기 자신과의 1:1 채팅은 만들 수 없습니다.");
        }

        Long emp1 = (myMemberId < targetMemberId) ? myMemberId : targetMemberId;
        Long emp2 = (myMemberId < targetMemberId) ? targetMemberId : myMemberId;

        // 1) 기존 DIRECT 방 조회
        ChatRoom room = chatRoomRepository
                .findByRoomTypeAndDirectEmp1AndDirectEmp2("DIRECT", emp1, emp2)
                .orElseGet(() -> {
                    // 2) 없으면 생성
                    ChatRoom newRoom = new ChatRoom();
                    newRoom.setRoomType("DIRECT");
                    newRoom.setCreatedBy(myMemberId);
                    newRoom.setCreatedAt(LocalDateTime.now()); // DB default가 있으면 생략 가능
                    newRoom.setDirectEmp1(emp1);
                    newRoom.setDirectEmp2(emp2);

                    return chatRoomRepository.save(newRoom);
                });

        // 3) 두 사람 참가 처리 (이미 있으면 무시)
        safeJoin(room.getId(), myMemberId);
        safeJoin(room.getId(), targetMemberId);

        return room.getId();
    }

    private void safeJoin(Long roomId, Long memberId) {
        ChatRoomMemberId pk = new ChatRoomMemberId(roomId, memberId);

        if (!chatRoomMemberRepository.existsById(pk)) {
            ChatRoomMember m = ChatRoomMember.of(roomId, memberId);
            m.setJoinedAt(LocalDateTime.now()); // joinedAt 세터는 Lombok @Setter로 있음
            chatRoomMemberRepository.save(m);
        }
    }
    
    public List<MemberDeptRowDTO> getMemberDeptRows() {
	    List<Object[]> rows = messengerRepository.findMemberDeptRows();

	    return rows.stream()
	        .map(r -> new MemberDeptRowDTO(
	            ((Number) r[0]).longValue(), // memberId
	            (String) r[1],              // memberName
	            (String) r[2]               // departmentName
	        ))
	        .toList();
	}
    
 // 1. 과거 채팅 내역 가져오기
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDTO> getChatHistory(Long roomId, Long myId) {
        // 나의 참여 정보(joinedAt)를 먼저 가져옵니다.
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, myId))
                .orElseThrow(() -> new RuntimeException("참여 정보가 없습니다."));

        // 간단해진 레포지토리 메서드 호출
        List<ChatMessage> messages = chatMessageRepository.findHistory(roomId, membership.getJoinedAt());

        return messages.stream().map(msg -> {
            ChatMessageResponseDTO dto = new ChatMessageResponseDTO();
            dto.setMemberId(msg.getMemberId());
            dto.setContent(msg.getContent());
            dto.setMsgType(msg.getMsgType());
            dto.setCreatedAt(msg.getCreatedAt());
            dto.setMemberName(messengerRepository.findById(msg.getMemberId()).map(Member::getName).orElse("알 수 없는 사용자"));
            return dto;
        }).collect(Collectors.toList());
    }

    // 2. 채팅방 상단에 표시할 이름 결정하기
    public String getRoomDisplayTitle(Long roomId, Long myMemberId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다."));

        if ("DIRECT".equals(room.getRoomType())) {
            // 1:1 채팅: 두 명의 참여자 중 내가 아닌 사람의 ID를 찾음
            Long partnerId = room.getDirectEmp1().equals(myMemberId) 
                             ? room.getDirectEmp2() 
                             : room.getDirectEmp1();
            
            // 상대방의 정보를 조회 (이름 반환)
            return messengerRepository.findById(partnerId)
                    .map(member -> member.getName()) // Member 엔티티에 getName()이 있다고 가정
                    .orElse("알 수 없는 사용자");
        } else {
            // 그룹 채팅: 설정된 방 이름을 사용 (구현 예정이라면 기본값 설정)
            return room.getRoomName() != null ? room.getRoomName() : "그룹 채팅방";
        }
    }
    
    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDTO> getChatRoomList(Long myId) {
        // 1. 내가 참여 중인 모든 방의 멤버 정보 조회
        List<ChatRoomMember> memberships = chatRoomMemberRepository.findByMemberId(myId);

        return memberships.stream().map(m -> {
            ChatRoom room = chatRoomRepository.findById(m.getId().getRoomId()).orElse(null);
            if (room == null) return null;

            ChatRoomListResponseDTO dto = new ChatRoomListResponseDTO();
            dto.setRoomId(room.getId());

            if ("DIRECT".equals(room.getRoomType())) {
                Long partnerId = room.getDirectEmp1().equals(myId) ? room.getDirectEmp2() : room.getDirectEmp1();
                String partnerName = messengerRepository.findById(partnerId)
                        .map(Member::getName).orElse("알 수 없는 사용자");
                dto.setRoomTitle(partnerName);
            } else {
                dto.setRoomTitle(room.getRoomName() != null ? room.getRoomName() : "그룹 채팅방");
            }

            List<ChatMessage> latest = chatMessageRepository.findLatest(room.getId(), m.getJoinedAt(), PageRequest.of(0, 1));
            
            // 데이터가 있을 때만 DTO에 세팅합니다.
            if (!latest.isEmpty()) {
                ChatMessage last = latest.get(0);
                dto.setLastMessage(last.getContent());
                dto.setLastTime(last.getCreatedAt().format(DateTimeFormatter.ofPattern("a h:mm")));
                dto.setLastMessageAt(last.getCreatedAt());
            }

            return dto;
        })
        .filter(Objects::nonNull)
        // 4. 마지막 메시지 시간 순으로 정렬 (최신순)
        .sorted((a, b) -> {
            if (a.getLastMessageAt() == null) return 1;
            if (b.getLastMessageAt() == null) return -1;
            return b.getLastMessageAt().compareTo(a.getLastMessageAt());
        })
        .collect(Collectors.toList());
    }
    

    @Transactional
    public Long createGroupRoom(String roomName, List<Long> memberIds, Long creatorId) {
        // 1. 그룹 채팅방 엔티티 생성 및 저장
        ChatRoom newRoom = new ChatRoom();
        newRoom.setRoomType("GROUP");
        newRoom.setRoomName(roomName);
        newRoom.setCreatedBy(creatorId);
        newRoom.setCreatedAt(LocalDateTime.now());
        
        ChatRoom savedRoom = chatRoomRepository.save(newRoom);

        // 2. 생성자 본인 참여 처리
        safeJoin(savedRoom.getId(), creatorId);

        // 3. 초대된 멤버들 참여 처리
        if (memberIds != null) {
            for (Long memberId : memberIds) {
                safeJoin(savedRoom.getId(), memberId);
            }
        }

        return savedRoom.getId();
    }

    @Transactional
    public ChatBroadcastMessageDTO leaveChatRoom(Long roomId, Long memberId) {
        // 1. 나가는 사람의 이름 찾기
        String memberName = messengerRepository.findById(memberId)
                .map(Member::getName).orElse("알 수 없는 사용자");

        // 2. 참여자 테이블에서 삭제
        chatRoomMemberRepository.deleteById(new ChatRoomMemberId(roomId, memberId));

        // 3. 시스템 메시지 생성 및 DB 저장
        ChatMessage systemMsg = new ChatMessage();
        systemMsg.setRoomId(roomId);
        systemMsg.setMemberId(memberId); // 누가 나갔는지 기록
        systemMsg.setContent(memberName + "님이 퇴장하셨습니다.");
        systemMsg.setMsgType("SYSTEM"); // ★ 타입을 SYSTEM으로 지정
        chatMessageRepository.save(systemMsg);

        // 4. 웹소켓 전송을 위한 DTO 반환
        ChatBroadcastMessageDTO dto = new ChatBroadcastMessageDTO();
        dto.setRoomId(roomId);
        dto.setContent(systemMsg.getContent());
        dto.setMsgType("SYSTEM");
        dto.setFormattedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("a h:mm")));
        return dto;
    }

    @Transactional
    public void updateRoomName(Long roomId, String newName) {
        ChatRoom room = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다."));
        room.setRoomName(newName); // 엔티티 수정 시 Dirty Checking으로 자동 업데이트
    }
}
