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
import com.itwillbs.ilkwangtech.messenger.repository.ChatAttachmentRepository;
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
    private final ChatAttachmentRepository chatAttachmentRepository;

    public MessengerService(ChatRoomRepository chatRoomRepository,
                            ChatRoomMemberRepository chatRoomMemberRepository, 
                            MessengerRepository messengerRepository, 
                            ChatMessageRepository chatMessageRepository, 
                            ChatRoomSettingRepository settingRepository,
                            MemberRepository memeberRepository,
                            DepartmentRepository departmentRepository, ChatAttachmentRepository chatAttachmentRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.messengerRepository = messengerRepository;
		this.settingRepository = settingRepository;
		this.memberRepository = memeberRepository;
		this.departmentRepository = departmentRepository;
		this.chatAttachmentRepository = chatAttachmentRepository;
    }

    // 1대1 채팅방 존재 여부 확인 후 없으면 상대 이름을 채팅방명으로 지정
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

        String myName = messengerRepository.findById(myMemberId).map(Member::getName).orElse("나");
        String partnerName = messengerRepository.findById(targetMemberId).map(Member::getName).orElse("상대방");

        safeJoin(room.getId(), myMemberId, partnerName); 
        safeJoin(room.getId(), targetMemberId, myName); 

        return room.getId();
    }

    // 채팅방 중복 가입 방지
    private void safeJoin(Long roomId, Long memberId, String nickname) {
        ChatRoomMemberId pk = new ChatRoomMemberId(roomId, memberId);
        if (!chatRoomMemberRepository.existsById(pk)) {
            ChatRoomMember m = ChatRoomMember.of(roomId, memberId, nickname); 
            m.setJoinedAt(LocalDateTime.now());
            chatRoomMemberRepository.save(m);
        }
    }
    
    // 전체 사원 리스트를 DTO 형태로 변환 후 조회
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
    
    // 사용자가 메시지를 확인한 가장 최근 시간 기록(읽음 처리 관리)
    @Transactional
    public void updateLastReadAt(Long roomId, Long memberId) {
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, memberId))
                .orElseThrow();
        membership.setLastReadAt(LocalDateTime.now());
    }
    
    // 메세지 전송 시간을 기준으로 해당 메시지를 읽지 않은 참여자 수 계산
    public int getUnreadCount(Long roomId, LocalDateTime messageTime) {
        List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(roomId);
        
        return (int) members.stream()
                .filter(m -> m.getLastReadAt() == null || m.getLastReadAt().isBefore(messageTime))
                .count();
    }
    
    // 과거 채팅 내역 조회
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDTO> getChatHistory(Long roomId, Long myId) {
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, myId)).orElseThrow();
        List<ChatMessage> messages = chatMessageRepository.findHistory(roomId, membership.getJoinedAt());

        return messages.stream().map(msg -> {
            ChatMessageResponseDTO dto = new ChatMessageResponseDTO();
            dto.setMemberId(msg.getMemberId());
            dto.setContent(msg.getContent());
            dto.setCreatedAt(msg.getCreatedAt());
            dto.setMsgType(msg.getMsgType());
            
            // [중요] DB에서 해당 메시지의 첨부파일 ID를 찾아 DTO에 담습니다.
            chatAttachmentRepository.findByMessageId(msg.getId()).ifPresent(attach -> {
                dto.setAttachId(attach.getId());
            });

            // 발신자 이름 및 시간 세팅
            String senderName = memberRepository.findById(msg.getMemberId()).map(Member::getName).orElse("사용자");
            dto.setMemberName(senderName);
            dto.setFormattedTime(msg.getCreatedAt().format(DateTimeFormatter.ofPattern("a h:mm")));
            dto.setUnreadCount(getUnreadCount(roomId, msg.getCreatedAt())); 
            
            return dto;
        }).collect(Collectors.toList());
    }

    // 사용자가 설정한 별명이 있으면 별명으로, 상대 이름이나 기본 방 이름 조회
    public String getRoomDisplayTitle(Long roomId, Long myMemberId) {
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, myMemberId))
                .orElse(null);
        
        if (membership != null && membership.getRoomNickname() != null && !membership.getRoomNickname().isEmpty()) {
            return membership.getRoomNickname();
        }

        // 별명이 없을 경우에만 기존 로직 수행 
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
        if ("DIRECT".equals(room.getRoomType())) {
            Long partnerId = room.getDirectEmp1().equals(myMemberId) ? room.getDirectEmp2() : room.getDirectEmp1();
            return messengerRepository.findById(partnerId).map(Member::getName).orElse("사용자");
        }
        return room.getRoomName() != null ? room.getRoomName() : "그룹 채팅방";
    }
    
    // 내가 참여중인 채팅방 목록 조회
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

            // 즐겨찾기 상태 조회
            settingRepository.findById(new ChatRoomMemberSettingId(room.getId(), myId))
                .ifPresent(s -> dto.setIsFavorite(s.getIsFavorite()));

            // 최신 메시지 조회
            Optional<ChatMessage> latest = chatMessageRepository.findLatest(room.getId(), m.getJoinedAt(), PageRequest.of(0, 1))
                    .stream().findFirst();
            latest.ifPresent(last -> {
                dto.setLastMessage(last.getContent());
                dto.setLastTime(last.getCreatedAt().format(DateTimeFormatter.ofPattern("a h:mm")));
            });
            
            LocalDateTime lastRead = (m.getLastReadAt() != null) ? m.getLastReadAt() : m.getJoinedAt();
            int count = (int) chatMessageRepository.findByRoomId(room.getId()).stream()
                .filter(msg -> msg.getCreatedAt().isAfter(lastRead))
                .filter(msg -> !msg.getMemberId().equals(myId))
                .count();
            dto.setUnreadCount(count);

            return dto;
        })
        .filter(Objects::nonNull)
        // 즐겨찾기가 최상단에 위치하고, 그 다음 최신 메시지 시간순으로 정렬
        .sorted((a, b) -> {
            if ("Y".equals(a.getIsFavorite()) && !"Y".equals(b.getIsFavorite())) return -1;
            if (!"Y".equals(a.getIsFavorite()) && "Y".equals(b.getIsFavorite())) return 1;
            
            if (a.getLastTime() == null) return 1;
            if (b.getLastTime() == null) return -1;
            return b.getLastTime().compareTo(a.getLastTime());
        })
        .collect(Collectors.toList());
    }
    
    // 그룹 채팅 생성
    @Transactional
    public Long createGroupRoom(String roomName, List<Long> memberIds, Long creatorId) {
        ChatRoom newRoom = new ChatRoom();
        newRoom.setRoomType("GROUP");
        newRoom.setRoomName(roomName); 
        newRoom.setCreatedBy(creatorId);
        newRoom.setCreatedAt(LocalDateTime.now());
        ChatRoom savedRoom = chatRoomRepository.save(newRoom);

        List<String> names = messengerRepository.findAllById(memberIds).stream()
                                .map(Member::getName).collect(Collectors.toList());
        String defaultName = roomName != null && !roomName.isEmpty() ? roomName : String.join(", ", names);

        safeJoin(savedRoom.getId(), creatorId, defaultName);
        if (memberIds != null) {
            for (Long memberId : memberIds) {
                safeJoin(savedRoom.getId(), memberId, defaultName);
            }
        }
        return savedRoom.getId();
    }

    // 채팅 나가지(참여 정보 삭제, 퇴장 안내 메세지 생성)
    @Transactional
    public ChatBroadcastMessageDTO leaveChatRoom(Long roomId, Long memberId) {
        // 나가는 사람의 이름 찾기
        String memberName = messengerRepository.findById(memberId)
                .map(Member::getName).orElse("알 수 없는 사용자");

        // 참여자 테이블에서 삭제
        chatRoomMemberRepository.deleteById(new ChatRoomMemberId(roomId, memberId));

        // 시스템 메시지 생성 및 DB 저장
        ChatMessage systemMsg = new ChatMessage();
        systemMsg.setRoomId(roomId);
        systemMsg.setMemberId(memberId); // 누가 나갔는지 기록
        systemMsg.setContent(memberName + "님이 퇴장하셨습니다.");
        systemMsg.setMsgType("SYSTEM"); // 타입을 SYSTEM으로 지정
        chatMessageRepository.save(systemMsg);

        // 웹소켓 전송 위한 DTO 반환
        ChatBroadcastMessageDTO dto = new ChatBroadcastMessageDTO();
        dto.setRoomId(roomId);
        dto.setContent(systemMsg.getContent());
        dto.setMsgType("SYSTEM");
        dto.setFormattedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("a h:mm")));
        return dto;
    }

    // 채팅방 이름 수정
    @Transactional
    public void updateRoomNickname(Long roomId, Long memberId, String newName) {
        ChatRoomMember membership = chatRoomMemberRepository.findById(new ChatRoomMemberId(roomId, memberId))
                .orElseThrow(() -> new RuntimeException("참여 정보를 찾을 수 없습니다."));
        
        membership.setRoomNickname(newName); 
    }

    // 특정 채팅방에 대한 즐겨찾기 상태를 DB에 기록
    @Transactional
    public void updateFavoriteStatus(Long roomId, Long memberId, String status) {
        // 복합 ID 생성
        ChatRoomMemberSettingId settingId = new ChatRoomMemberSettingId(roomId, memberId);

        // 기존 설정 존재 여부 확인
        ChatRoomMemberSetting setting = settingRepository.findById(settingId)
                .orElseGet(() -> {
                    // 데이터가 없으면 새 객체 생성 및 기본값 세팅
                    ChatRoomMemberSetting newSetting = new ChatRoomMemberSetting();
                    newSetting.setId(settingId);
                    return newSetting;
                });

        // 상태 업데이트 및 날짜 처리
        setting.setIsFavorite(status);
        if ("Y".equals(status)) {
            setting.setFavoritedAt(LocalDateTime.now()); // 즐겨찾기 지정 일시
        } else {
            setting.setFavoritedAt(null); // 즐겨찾기 해제 시 날짜 삭제
        }

        settingRepository.save(setting);
    }
    
    // 사원목록 조회
    public List<MemberDeptRowDTO> getMemberListWithFavorite(Long myId) {
        List<Member> members = memberRepository.findAll();
        
        return members.stream().map(m -> {
            MemberDeptRowDTO dto = new MemberDeptRowDTO();
            dto.setMemberId(m.getId());
            dto.setMemberName(m.getName());
            
            // 부서 정보 세팅 
            Integer deptId = m.getDepartment(); 
            String dName = (deptId != null) ? 
                departmentRepository.findById(deptId).map(Department::getDepartmentName).orElse("소속 없음") 
                : "소속 없음";
            dto.setDepartmentName(dName);
            
            // 채팅방의 ID를 기반으로 즐겨찾기 여부를 확인
            Long emp1 = (myId < m.getId()) ? myId : m.getId();
            Long emp2 = (myId < m.getId()) ? m.getId() : myId;

            chatRoomRepository.findByRoomTypeAndDirectEmp1AndDirectEmp2("DIRECT", emp1, emp2)
                .ifPresent(room -> {
                    // 방 ID와 내 ID를 조합한 복합키로 설정 조회
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
        List<ChatRoomMember> memberships = chatRoomMemberRepository.findByMemberId(myId);
        
        for (ChatRoomMember m : memberships) {
            LocalDateTime lastRead = (m.getLastReadAt() != null) ? m.getLastReadAt() : m.getJoinedAt();
            
            // 내가 보낸 메시지는 제외하고 카운트
            long count = chatMessageRepository.countByRoomIdAndCreatedAtAfterAndMemberIdNot(
                m.getId().getRoomId(), lastRead, myId
            );
            
            if (count > 0) return true;
        }
        return false;
    }
}
