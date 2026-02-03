package com.itwillbs.ilkwangtech.messenger.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
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

	@GetMapping("/memberList")
	public String memberList(Model model, Authentication auth) {
	    Long myId = ((AccountLogin)auth.getPrincipal()).getId();
	    List<MemberDeptRowDTO> memberList = messengerService.getMemberListWithFavorite(myId);
	    
	    model.addAttribute("myMemberId", myId);
	    model.addAttribute("memberList", memberList);
	    
	    return "messenger/memberList"; // "/messenger/memberList"에서 앞의 슬래시 제거
	}
	
	@GetMapping("/chatList")
	public String chatList(@AuthenticationPrincipal AccountLogin login, Model model) {
	    
	    // 1. 현재 로그인한 사용자의 ID로 참여 중인 채팅방 목록 조회
	    List<ChatRoomListResponseDTO> rooms = messengerService.getChatRoomList(login.getId());
	    model.addAttribute("rooms", rooms); 
	    
	    model.addAttribute("memberList", messengerService.getMemberDeptRows()); 
	    
	    return "messenger/chatList"; 
	}
	
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
	        // 자기 자신과의 채팅 시도 등 예외 발생 시 처리
	        // 자바스크립트로 알림을 띄우고 뒤로가기(또는 목록 이동) 처리
	        model.addAttribute("msg", e.getMessage()); // "자기 자신과의 1:1 채팅은..."
	        model.addAttribute("url", "/messenger/memberList"); // 이동할 경로
	        return "common/messageRedirect"; // 공통 알림 페이지(없다면 새로 만들어야 함)
	    }
	}
	
	@GetMapping("/direct/{targetId}")
	public String openDirect(@PathVariable("targetId") Long targetId, 
	                         @AuthenticationPrincipal AccountLogin login) {
	    // 서비스 내부에서 이미 상대방 이름을 내 별명으로 초기화하도록 구현됨
	    Long roomId = messengerService.getOrCreateDirectRoom(login.getId(), targetId);
	    
	    return "redirect:/messenger/chatroom/" + roomId;
	    // 임시
	}


	@GetMapping("/chatroom/{roomId}")
	public String chatroom(@PathVariable("roomId") Long roomId, 
	                       @AuthenticationPrincipal AccountLogin login, 
	                       Model model) {
	    Long myId = login.getId();

	    model.addAttribute("displayTitle", messengerService.getRoomDisplayTitle(roomId, myId));
	    
	    List<ChatMessageResponseDTO> chatHistory = messengerService.getChatHistory(roomId, myId); 
	    
	    model.addAttribute("isFavorite", messengerService.getFavoriteStatus(roomId, myId));
	    model.addAttribute("chatHistory", chatHistory);
	    model.addAttribute("roomId", roomId);
	    model.addAttribute("myMemberId", myId);

	    return "messenger/chatroom";
	}
	
	// [추가] 특정 방의 채팅 내역을 JSON으로 반환하는 API
	@GetMapping("/api/chat/{roomId}")
	@ResponseBody
	public List<ChatMessageResponseDTO> getChatHistoryApi(@PathVariable("roomId") Long roomId,
	                                                       @AuthenticationPrincipal AccountLogin login) {
	    return messengerService.getChatHistory(roomId, login.getId()); 
	}

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
    
    @PostMapping("/api/rename")
    @ResponseBody
    public String renameRoom(@RequestBody Map<String, Object> params, 
                             @AuthenticationPrincipal AccountLogin login) {
        // 1. 파라미터 추출 (roomId는 숫자형으로, newName은 문자열로 변환)
        Long roomId = Long.valueOf(params.get("roomId").toString());
        String newName = params.get("newName").toString();
        
        // 2. 서비스 호출: ChatRoom이 아닌 내 참여 정보(ChatRoomMember)의 별명을 수정
        messengerService.updateRoomNickname(roomId, login.getId(), newName);
        
        return "success";
    }

    
    @PostMapping("/api/read/{roomId}")
    @ResponseBody
    public String readMessages(@PathVariable("roomId") Long roomId, @AuthenticationPrincipal AccountLogin login) {
        Long myId = login.getId();
        messengerService.updateLastReadAt(roomId, myId);
        
        // 1. 채팅방 내부용 신호 (숫자 1 제거용)
        ChatBroadcastMessageDTO readSignal = new ChatBroadcastMessageDTO();
        readSignal.setRoomId(roomId);
        readSignal.setMsgType("READ");
        readSignal.setMemberId(myId);
        simpMessagingTemplate.convertAndSend("/topic/chatroom/" + roomId, readSignal);
        
        // 읽음 처리
        simpMessagingTemplate.convertAndSend("/topic/user/" + myId + "/list", readSignal);
        
        return "success";
    }
    
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
    
    @PostMapping("/api/favorite")
    @ResponseBody
    public ResponseEntity<String> toggleFavorite(@RequestBody Map<String, Object> params) {
        Long roomId = Long.valueOf(params.get("roomId").toString());
        Long memberId = Long.valueOf(params.get("myMemberId").toString());
        String status = params.get("status").toString(); // "Y" 또는 "N"

        // CHAT_ROOM_MEMBER_SETTING 테이블에 상태 업데이트 또는 Insert 로직 수행
        messengerService.updateFavoriteStatus(roomId, memberId, status);
        
        return ResponseEntity.ok("success");
    }
    
    @PostMapping("/api/favorite/member")
    @ResponseBody
    public ResponseEntity<String> toggleMemberFavorite(@RequestBody Map<String, Object> params) {
        Long myId = Long.valueOf(params.get("myMemberId").toString());
        Long targetId = Long.valueOf(params.get("targetMemberId").toString());
        String status = params.get("status").toString();

        // 1. 나와 상대방의 1:1 채팅방 ID를 가져옵니다. (없으면 생성됨)
        Long roomId = messengerService.getOrCreateDirectRoom(myId, targetId);

        // 2. 해당 방에 대한 즐겨찾기 설정을 업데이트합니다.
        messengerService.updateFavoriteStatus(roomId, myId, status);
        
        return ResponseEntity.ok("success");
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
 // MessengerController.java 내부의 downloadFile 함수 전체
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

        // [수정] 이미지면 inline(화면표시), 아니면 attachment(다운로드)
        String dispositionType = (contentType.startsWith("image")) ? "inline" : "attachment";

        ContentDisposition contentDisposition = ContentDisposition.builder(dispositionType)
                .filename(attach.getOriginalName(), java.nio.charset.StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(resource);
    }
    
    
}










