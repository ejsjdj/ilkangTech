package com.itwillbs.ilkwangtech.messenger.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.messenger.dto.ChatBroadcastMessageDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatMessageResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatRoomListResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.MemberDeptRowDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatAttachment;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMember;
import com.itwillbs.ilkwangtech.messenger.repository.ChatAttachmentRepository;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomMemberRepository;
import com.itwillbs.ilkwangtech.messenger.service.ChatFileService;
import com.itwillbs.ilkwangtech.messenger.service.MessengerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/messenger")
public class MessengerController {
	
	@Value("${file.uploadBaseLocation}")
    private String uploadBaseLocation;

    @Value("${file.chatFileLocation}")
    private String chatFileLocation;
	
	private final MessengerService messengerService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final DepartmentRepository departmentRepository;
	private final PositionRepository positionRepository;
	private final ChatFileService chatFileService;
	private final ChatAttachmentRepository chatAttachmentRepository;
	
	public MessengerController(MessengerService messengerService, SimpMessagingTemplate simpMessagingTemplate, ChatRoomMemberRepository chatRoomMemberRepository, PositionRepository positionRepository, DepartmentRepository departmentRepository, ChatFileService chatFileService, ChatAttachmentRepository chatAttachmentRepository) {
		this.messengerService = messengerService;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.chatRoomMemberRepository = chatRoomMemberRepository;
		this.departmentRepository = departmentRepository;
		this.positionRepository = positionRepository;
		this.chatFileService = chatFileService;
		this.chatAttachmentRepository = chatAttachmentRepository;
	}

	// 사원 리스트 조회 요청
	@GetMapping("/memberList")
	public String memberList(Model model, Authentication auth) {
	    Long myId = ((AccountLogin)auth.getPrincipal()).getId();
	    List<MemberDeptRowDTO> memberList = messengerService.getMemberListWithFavorite(myId);
	    
	    model.addAttribute("myMemberId", myId);
	    model.addAttribute("memberList", memberList);
	    
	    return "messenger/memberList"; 
	}
	
	// 채팅 리스트 조회 요청
	@GetMapping("/chatList")
	public String chatList(@AuthenticationPrincipal AccountLogin login, Model model) {
	    
	    // 현재 로그인한 사용자의 ID로 참여 중인 채팅방 목록 조회
	    List<ChatRoomListResponseDTO> rooms = messengerService.getChatRoomList(login.getId());
	    model.addAttribute("rooms", rooms); 
	    model.addAttribute("memberList", messengerService.getMemberDeptRows());
	    model.addAttribute("myMemberId", login.getId());
	    
	    return "messenger/chatList"; 
	}
	
	// 특정 사원과의 1대1 채팅방 존재 여부 조회
	@GetMapping("/direct")
	public String openDirect(@RequestParam("memberId") Long targetMemberId,
	                         @AuthenticationPrincipal AccountLogin login,
	                         Model model) {

	    Long myMemberId = login.getId();
	    try {
	        // 서비스 호출
	        Long roomId = messengerService.getOrCreateDirectRoom(myMemberId, targetMemberId);
	        System.out.println("openDirect roomId = " + roomId);
	        return "redirect:/messenger/chatroom/" + roomId;

	    } catch (IllegalArgumentException e) {
	        
	        // 자바스크립트로 알림을 띄우고 뒤로가기(또는 목록 이동) 처리
	        model.addAttribute("msg", e.getMessage()); 
	        model.addAttribute("url", "/messenger/memberList"); 
	        return "common/messageRedirect"; 
	    }
	}
	
	// 
	@GetMapping("/direct/{targetId}")
	public String openDirect(@PathVariable("targetId") Long targetId, 
	                         @AuthenticationPrincipal AccountLogin login) {
	    // 서비스 내부에서 이미 상대방 이름을 내 별명으로 초기화하도록 구현됨
	    Long roomId = messengerService.getOrCreateDirectRoom(login.getId(), targetId);
	    
	    return "redirect:/messenger/chatroom/" + roomId;
	}

	// 해당 채팅방의 상세 페이지 호출
	@GetMapping("/chatroom/{roomId}")
	public String chatroom(@PathVariable("roomId") Long roomId, 
	                       @AuthenticationPrincipal AccountLogin login, 
	                       Model model) {
	    Long myId = login.getId();
	    
	    System.out.println("=== 채팅방 입장 확인 ===");
	    System.out.println("현재 로그인한 사용자 ID (myId): " + myId);

	    model.addAttribute("displayTitle", messengerService.getRoomDisplayTitle(roomId, myId));
	    
	    List<ChatMessageResponseDTO> chatHistory = messengerService.getChatHistory(roomId, myId); 
	    
	    model.addAttribute("isFavorite", messengerService.getFavoriteStatus(roomId, myId));
	    model.addAttribute("chatHistory", chatHistory);
	    model.addAttribute("roomId", roomId);
	    model.addAttribute("myMemberId", myId);

	    return "messenger/chatroom";
	}
	
	// 특정 방의 채팅 내역을 JSON으로 반환하는 API
	@GetMapping("/api/chat/{roomId}")
	@ResponseBody
	public List<ChatMessageResponseDTO> getChatHistoryApi(@PathVariable("roomId") Long roomId,
	                                                       @AuthenticationPrincipal AccountLogin login) {
	    return messengerService.getChatHistory(roomId, login.getId()); 
	}

	// 그룹 채팅방 생성
	@PostMapping("/createGroup")
	@ResponseBody
	public Long createGroup(@RequestBody Map<String, Object> params, 
	                        @AuthenticationPrincipal AccountLogin login) {
	    String roomName = (String) params.get("roomName");
	    List<Integer> ids = (List<Integer>) params.get("memberIds");
	    List<Long> memberIds = ids.stream().map(Long::valueOf).collect(Collectors.toList());

	    // 모든 참여자에게 '참여자 이름들'을 초기 별명으로 부여하는 서비스 호출
	    return messengerService.createGroupRoom(roomName, memberIds, login.getId());
	}
    
    // 채팅방 퇴장 처리
    @PostMapping("/api/leave/{roomId}")
    @ResponseBody
    public String leaveRoomApi(@PathVariable("roomId") Long roomId, 
                               @AuthenticationPrincipal AccountLogin login) {
        // 1. 서비스 호출하여 DB에서 삭제
    	ChatBroadcastMessageDTO systemMsg = messengerService.leaveChatRoom(roomId, login.getId());
    	simpMessagingTemplate.convertAndSend("/topic/chatroom/" + roomId, systemMsg);
    	
    	List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(roomId);
        for (ChatRoomMember m : members) {
            // 각 멤버의 개인 리스트 갱신 토픽으로 시스템 메시지 전송
            simpMessagingTemplate.convertAndSend("/topic/user/" + m.getId().getMemberId() + "/list", systemMsg);
        }
    	
        return "success";
    }
    
    // 채팅방 이름 수정
    @PostMapping("/api/rename")
    @ResponseBody
    public String renameRoom(@RequestBody Map<String, Object> params, 
                             @AuthenticationPrincipal AccountLogin login) {
    	
        Long roomId = Long.valueOf(params.get("roomId").toString());
        String newName = params.get("newName").toString();
        
        messengerService.updateRoomNickname(roomId, login.getId(), newName);
        
        return "success";
    }

    // 채팅 읽음 처리
    @PostMapping("/api/read/{roomId}")
    @ResponseBody
    public String readMessages(@PathVariable("roomId") Long roomId, 
    						   @RequestParam(value = "lastTime", required = false) String lastTime,
    						   @AuthenticationPrincipal AccountLogin login) {
        Long myId = login.getId();
        
        // 파라미터로 받은 시간이 있으면 그것을 사용, 없으면 현재시간 사용
        LocalDateTime readTime = (lastTime != null) ? LocalDateTime.parse(lastTime) : LocalDateTime.now();
        messengerService.updateLastReadAt(roomId, myId, readTime);
        
        // 채팅방 내부용 신호 (숫자 1 제거용)
        ChatBroadcastMessageDTO readSignal = new ChatBroadcastMessageDTO();
        readSignal.setRoomId(roomId);
        readSignal.setMsgType("READ");
        readSignal.setMemberId(myId);
        simpMessagingTemplate.convertAndSend("/topic/chatroom/" + roomId, readSignal);
        
        List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(roomId);
        for (ChatRoomMember m : members) {
            simpMessagingTemplate.convertAndSend("/topic/user/" + m.getId().getMemberId() + "/list", readSignal);
        }
        
        return "success";
    }
    
    // 현재 채팅방에 속해있는 사원들 정보 조회
    @GetMapping("/api/members/{roomId}")
    @ResponseBody
    public List<ChatMessageResponseDTO> getChatParticipants(@PathVariable("roomId") Long roomId) {
        List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(roomId);
        
        return members.stream().map(m -> {
            ChatMessageResponseDTO dto = new ChatMessageResponseDTO();
            dto.setMemberId(m.getId().getMemberId());
            
            if (m.getMember() != null) {
                dto.setMemberName(m.getMember().getName());
                
                // 1. 부서 ID(Integer)로 부서명 찾기
                Integer deptId = m.getMember().getDepartment(); // getDepartment()가 Integer를 반환할 때
                if (deptId != null) {
                    String dName = departmentRepository.findById(deptId)
                                    .map(Department::getDepartmentName)
                                    .orElse("소속 없음");
                    dto.setDeptName(dName);
                }

                // 2. 직급 ID(Integer)로 직급명 찾기
                Integer posId = m.getMember().getPosition(); // getPosition()이 Integer를 반환할 때
                if (posId != null) {
                    String pName = positionRepository.findById(posId)
                                    .map(Position::getPositionName)
                                    .orElse("직급 없음");
                    dto.setPositionName(pName);
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }
    
    // 채팅창에 대한 즐겨찾기 설정
    @PostMapping("/api/favorite")
    public ResponseEntity<?> toggleFavorite(@RequestBody Map<String, Object> params) {
        try {
            // 1. 데이터 추출
            Object roomIdObj = params.get("roomId");
            Object memberIdObj = params.get("myMemberId");
            Object statusObj = params.get("status");

            // 2. Null 체크
            if (roomIdObj == null || memberIdObj == null || statusObj == null) {
                return ResponseEntity.badRequest().body("필수 파라미터가 누락되었습니다.");
            }

            // 3. 타입 변환 (안전하게 처리)
            Long roomId = Long.parseLong(roomIdObj.toString());
            Long memberId = Long.parseLong(memberIdObj.toString());
            String status = statusObj.toString();

            // 4. 후속 로직: DB 업데이트 (본인의 Service 명칭에 맞게 수정하세요)
            messengerService.updateFavoriteStatus(roomId, memberId, status);
            // 지금은 서비스 호출 코드가 없으므로 로그로 대체하거나 본인의 코드를 넣으세요.
            System.out.println("즐겨찾기 업데이트: 방=" + roomId + ", 회원=" + memberId + ", 상태=" + status);

            // 5. 성공 응답 반환 (이게 있어야 빨간 줄이 사라집니다)
            return ResponseEntity.ok().body(Map.of("status", "success", "currentStatus", status));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("에러 발생: " + e.getMessage());
        }
    }
    
    @PostMapping("/api/favorite/member")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleMemberFavorite(@RequestBody Map<String, Object> params) {
        Long myId = Long.valueOf(params.get("myMemberId").toString());
        Long targetId = Long.valueOf(params.get("targetMemberId").toString());
        String status = params.get("status").toString();

        messengerService.toggleMemberFavorite(myId, targetId, status);
        
        Optional<Long> roomIdOpt = messengerService.findDirectRoomId(myId, targetId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        roomIdOpt.ifPresent(id -> response.put("roomId", id)); 
        
        return ResponseEntity.ok(response);
    }
    
 // 파일 업로드 API
    @PostMapping("/api/upload")
    @ResponseBody
    public ResponseEntity<ChatAttachment> uploadFile(@RequestParam("file") MultipartFile file, 
                                                     @RequestParam("roomId") Long roomId,
                                                     @AuthenticationPrincipal AccountLogin login) throws IOException {
        
        ChatAttachment savedAttach = chatFileService.saveChatFile(file, roomId, login.getId());
        
        return ResponseEntity.ok(savedAttach);
    }

    // 파일 다운로드 API
    @GetMapping("/download/{attachId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("attachId") Long attachId) throws IOException {
        ChatAttachment attach = chatAttachmentRepository.findById(attachId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

        Path path = Paths.get(uploadBaseLocation, attach.getStorePath())
                         .resolve(attach.getStoredName())
                         .normalize();
        
        Resource resource = new UrlResource(path.toUri());
        String contentType = Files.probeContentType(path);
        if(contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        String dispositionType = (contentType.startsWith("image")) ? "inline" : "attachment";

        ContentDisposition contentDisposition = ContentDisposition.builder(dispositionType)
                .filename(attach.getOriginalName(), java.nio.charset.StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(resource);
    }
    
    // 읽지 않은 메시지 있는지 확인
    @GetMapping("/api/unread-exists")
    @ResponseBody
    public boolean unreadExists(@AuthenticationPrincipal AccountLogin login) {
        if (login == null) return false;
        return messengerService.hasAnyUnread(login.getId());
    }
    
}










