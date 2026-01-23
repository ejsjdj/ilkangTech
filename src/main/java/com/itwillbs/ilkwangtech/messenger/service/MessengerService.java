package com.itwillbs.ilkwangtech.messenger.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<ChatMessage> getChatHistory(Long roomId) {
        return chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
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

}
