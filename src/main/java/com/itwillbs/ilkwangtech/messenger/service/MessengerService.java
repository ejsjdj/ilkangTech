package com.itwillbs.ilkwangtech.messenger.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.messenger.dto.ChatBroadcastMessageDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatMessageResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatRoomListResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.MemberDeptRowDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoom;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMember;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberId;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberSetting;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMemberSettingId;
import com.itwillbs.ilkwangtech.messenger.repository.ChatMessageRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomMemberRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomSettingRepository;
import com.itwillbs.ilkwangtech.messenger.repository.MessengerRepository;

@Service
public class MessengerService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MessengerRepository messengerRepository;
    private final ChatRoomSettingRepository settingRepository;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    public MessengerService(ChatRoomRepository chatRoomRepository,
                            ChatRoomMemberRepository chatRoomMemberRepository, 
                            MessengerRepository messengerRepository, 
                            ChatMessageRepository chatMessageRepository, 
                            ChatRoomSettingRepository settingRepository,
                            MemberRepository memeberRepository,
                            DepartmentRepository departmentRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.messengerRepository = messengerRepository;
		this.settingRepository = settingRepository;
		this.memberRepository = memeberRepository;
		this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Long getOrCreateDirectRoom(Long myMemberId, Long targetMemberId) {
        if (myMemberId == null || targetMemberId == null) {
            throw new IllegalArgumentException("memberId는 null일 수 없습니다.");
        }

        Long emp1 = (myMemberId < targetMemberId) ? myMemberId : targetMemberId;
        Long emp2 = (myMemberId < targetMemberId) ? targetMemberId : myMemberId;

        ChatRoom room = chatRoomRepository
                .findByRoomTypeAndDirectEmp1AndDirectEmp2("DIRECT", emp1, emp2)
                .orElseGet(() -> {
                    ChatRoom newRoom = new ChatRoom();
                    newRoom.setRoomType("DIRECT");
                    newRoom.setCreatedBy(myMemberId);
                    newRoom.setCreatedAt(LocalDateTime.now());
                    newRoom.setDirectEmp1(emp1);
                    newRoom.setDirectEmp2(emp2);
                    return chatRoomRepository.save(newRoom);
                });

        // ★ 수정: 참여 시 서로의 이름을 초기 별명으로 저장합니다.
        String myName = messengerRepository.findById(myMemberId).map(Member::getName).orElse("나");
        String partnerName = messengerRepository.findById(targetMemberId).map(Member::getName).orElse("상대방");

        safeJoin(room.getId(), myMemberId, partnerName); // 나에게는 상대방 이름이 초기 별명
        safeJoin(room.getId(), targetMemberId, myName);  // 상대방에게는 내 이름이 초기 별명

        return room.getId();
    }

    private void safeJoin(Long roomId, Long memberId, String nickname) {
        ChatRoomMemberId pk = new ChatRoomMemberId(roomId, memberId);
        if (!chatRoomMemberRepository.existsById(pk)) {
            // ★ 핵심: of() 메서드에 nickname 파라미터를 추가하여 에러를 해결합니다.
            ChatRoomMember m = ChatRoomMember.of(roomId, memberId, nickname); 
            m.setJoinedAt(LocalDateTime.now());
            // m.setRoomNickname(nickname); -> of() 내부에서 이미 처리하므로 삭제 가능
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
    
    @Transactional
    public void updateLastReadAt(Long roomId, Long memberId) {
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, memberId))
                .orElseThrow();
        membership.setLastReadAt(LocalDateTime.now()); // 현재 시간으로 갱신
    }
    
    public int getUnreadCount(Long roomId, LocalDateTime messageTime) {
        List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(roomId);
        
        return (int) members.stream()
                .filter(m -> m.getLastReadAt() == null || m.getLastReadAt().isBefore(messageTime))
                .count();
    }
    
 // 1. 과거 채팅 내역 가져오기
 // MessengerService.java

    @Transactional(readOnly = true)
    public List<ChatMessageResponseDTO> getChatHistory(Long roomId, Long myId) {
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, myId)).orElseThrow();
        List<ChatMessage> messages = chatMessageRepository.findHistory(roomId, membership.getJoinedAt());

        return messages.stream().map(msg -> {
            ChatMessageResponseDTO dto = new ChatMessageResponseDTO();
            
            // ★ 이 부분들이 채워져야 화면에 글자와 이름이 나옵니다!
            dto.setMemberId(msg.getMemberId());
            dto.setContent(msg.getContent());
            dto.setCreatedAt(msg.getCreatedAt());
            dto.setMsgType(msg.getMsgType());
            
            // 발신자 이름 조회
            String senderName = messengerRepository.findById(msg.getMemberId())
                    .map(Member::getName).orElse("알 수 없는 사용자");
            dto.setMemberName(senderName);
            
            // 시간 포맷 (오전 9:00 같은 형식)
            dto.setFormattedTime(msg.getCreatedAt().format(DateTimeFormatter.ofPattern("a h:mm")));
            
            // 안 읽은 수 계산 (추가된 기능)
            dto.setUnreadCount(getUnreadCount(roomId, msg.getCreatedAt())); 
            
            return dto;
        }).collect(Collectors.toList());
    }

    public String getRoomDisplayTitle(Long roomId, Long myMemberId) {
        // 1. 내 개인 닉네임 확인
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, myMemberId))
                .orElse(null);
        
        if (membership != null && membership.getRoomNickname() != null && !membership.getRoomNickname().isEmpty()) {
            return membership.getRoomNickname();
        }

        // 2. 별명이 없을 경우에만 기존 로직 수행 (하방 호환성용)
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
        if ("DIRECT".equals(room.getRoomType())) {
            Long partnerId = room.getDirectEmp1().equals(myMemberId) ? room.getDirectEmp2() : room.getDirectEmp1();
            return messengerRepository.findById(partnerId).map(Member::getName).orElse("사용자");
        }
        return room.getRoomName() != null ? room.getRoomName() : "그룹 채팅방";
    }
    
 // MessengerService.java

    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDTO> getChatRoomList(Long myId) {
        List<ChatRoomMember> memberships = chatRoomMemberRepository.findByMemberId(myId);

        return memberships.stream().map(m -> {
            ChatRoom room = chatRoomRepository.findById(m.getId().getRoomId()).orElse(null);
            if (room == null) return null;

            ChatRoomListResponseDTO dto = new ChatRoomListResponseDTO();
            dto.setRoomId(room.getId());

            // 내 별명을 제목으로 설정
            String title = (m.getRoomNickname() != null && !m.getRoomNickname().isEmpty()) 
                           ? m.getRoomNickname() : getRoomDisplayTitle(room.getId(), myId);
            dto.setRoomTitle(title);

            // [추가] 즐겨찾기 상태 조회
            settingRepository.findById(new ChatRoomMemberSettingId(room.getId(), myId))
                .ifPresent(s -> dto.setIsFavorite(s.getIsFavorite()));

            // 최신 메시지 조회 로직 (기존 유지)
            Optional<ChatMessage> latest = chatMessageRepository.findLatest(room.getId(), m.getJoinedAt(), PageRequest.of(0, 1))
                    .stream().findFirst();
            latest.ifPresent(last -> {
                dto.setLastMessage(last.getContent());
                dto.setLastTime(last.getCreatedAt().format(DateTimeFormatter.ofPattern("a h:mm")));
            });

            return dto;
        })
        .filter(Objects::nonNull)
        // [수정] 즐겨찾기(Y)가 최상단에 오고, 그 다음 최신 메시지 시간순으로 정렬
        .sorted((a, b) -> {
            if ("Y".equals(a.getIsFavorite()) && !"Y".equals(b.getIsFavorite())) return -1;
            if (!"Y".equals(a.getIsFavorite()) && "Y".equals(b.getIsFavorite())) return 1;
            
            if (a.getLastTime() == null) return 1;
            if (b.getLastTime() == null) return -1;
            return b.getLastTime().compareTo(a.getLastTime());
        })
        .collect(Collectors.toList());
    }
    

    @Transactional
    public Long createGroupRoom(String roomName, List<Long> memberIds, Long creatorId) {
        ChatRoom newRoom = new ChatRoom();
        newRoom.setRoomType("GROUP");
        newRoom.setRoomName(roomName); // 공용 이름은 참고용으로 저장
        newRoom.setCreatedBy(creatorId);
        newRoom.setCreatedAt(LocalDateTime.now());
        ChatRoom savedRoom = chatRoomRepository.save(newRoom);

        // ★ 초기 그룹 이름 생성 (예: "홍길동, 김철수, 이영희")
        List<String> names = messengerRepository.findAllById(memberIds).stream()
                                .map(Member::getName).collect(Collectors.toList());
        String defaultName = roomName != null && !roomName.isEmpty() ? roomName : String.join(", ", names);

        // 모든 참여자에게 초기 별명 부여
        safeJoin(savedRoom.getId(), creatorId, defaultName);
        if (memberIds != null) {
            for (Long memberId : memberIds) {
                safeJoin(savedRoom.getId(), memberId, defaultName);
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
    public void updateRoomNickname(Long roomId, Long memberId, String newName) {
        // 내 참여 정보를 찾아서 이름 수정
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, memberId))
                .orElseThrow(() -> new RuntimeException("참여 정보를 찾을 수 없습니다."));
        
        membership.setRoomNickname(newName); // Dirty Checking으로 자동 업데이트
    }

    @Transactional
    public void updateFavoriteStatus(Long roomId, Long memberId, String status) {
        // 1. 복합 ID 생성
        ChatRoomMemberSettingId settingId = new ChatRoomMemberSettingId(roomId, memberId);

        // 2. 기존 설정 존재 여부 확인
        ChatRoomMemberSetting setting = settingRepository.findById(settingId)
                .orElseGet(() -> {
                    // 데이터가 없으면 새 객체 생성 및 기본값 세팅
                    ChatRoomMemberSetting newSetting = new ChatRoomMemberSetting();
                    newSetting.setId(settingId);
                    return newSetting;
                });

        // 3. 상태 업데이트 및 날짜 처리
        setting.setIsFavorite(status);
        if ("Y".equals(status)) {
            setting.setFavoritedAt(LocalDateTime.now()); // 즐겨찾기 지정 일시
        } else {
            setting.setFavoritedAt(null); // 즐겨찾기 해제 시 날짜 삭제
        }

        // 4. 저장 (JPA Dirty Checking에 의해 생략 가능하나 명시적 호출도 무방)
        settingRepository.save(setting);
    }
    
    public List<MemberDeptRowDTO> getMemberListWithFavorite(Long myId) {
        List<Member> members = memberRepository.findAll();
        
        return members.stream().map(m -> {
            MemberDeptRowDTO dto = new MemberDeptRowDTO();
            dto.setMemberId(m.getId());
            dto.setMemberName(m.getName());
            
            // 부서 정보 세팅 (기존 로직 유지)
            Integer deptId = m.getDepartment(); 
            String dName = (deptId != null) ? 
                departmentRepository.findById(deptId).map(Department::getDepartmentName).orElse("소속 없음") 
                : "소속 없음";
            dto.setDepartmentName(dName);
            
            // [수정] 실제 1:1 채팅방의 ID를 기반으로 즐겨찾기 여부를 확인합니다.
            Long emp1 = (myId < m.getId()) ? myId : m.getId();
            Long emp2 = (myId < m.getId()) ? m.getId() : myId;

            chatRoomRepository.findByRoomTypeAndDirectEmp1AndDirectEmp2("DIRECT", emp1, emp2)
                .ifPresent(room -> {
                    // 방 ID와 내 ID를 조합한 복합키로 설정을 조회합니다.
                    settingRepository.findById(new ChatRoomMemberSettingId(room.getId(), myId))
                        .ifPresent(s -> dto.setIsFavorite(s.getIsFavorite()));
                });
            
            return dto;
        })
        .sorted(Comparator.comparing(MemberDeptRowDTO::getIsFavorite).reversed()
                .thenComparing(MemberDeptRowDTO::getMemberName))
        .collect(Collectors.toList());
    }
    
    public String getFavoriteStatus(Long roomId, Long memberId) {
        return settingRepository.findById(new ChatRoomMemberSettingId(roomId, memberId))
                .map(ChatRoomMemberSetting::getIsFavorite)
                .orElse("N"); // 설정이 없으면 기본값 'N'
    }
    
    // 새로운 메세지 표시 로직
    @Transactional(readOnly = true)
    public boolean hasAnyUnread(Long myId) {
        // 1. 내가 참여 중인 모든 방 정보를 가져옵니다.
        List<ChatRoomMember> memberships = chatRoomMemberRepository.findByMemberId(myId);
        
        for (ChatRoomMember m : memberships) {
            // 각 방에서 내가 마지막으로 읽은 시간 이후의 메시지가 있는지 확인
        	LocalDateTime lastRead = (m.getLastReadAt() != null) ? m.getLastReadAt() : m.getJoinedAt();
        	long count = chatMessageRepository.countByRoomIdAndCreatedAtAfter(m.getId().getRoomId(), lastRead);
            if (count > 0) return true;
        }
        return false;
    }
}
