package com.itwillbs.ilkwangtech.messenger.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.messenger.dto.ChatBroadcastMessageDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatMessageResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.ChatRoomListResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.MemberDeptRowDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoomMember;
import com.itwillbs.ilkwangtech.messenger.repository.ChatRoomMemberRepository;
import com.itwillbs.ilkwangtech.messenger.service.MessengerService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/messenger")
public class MessengerController {
	
	private final MessengerService messengerService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	
	public MessengerController(MessengerService messengerService, SimpMessagingTemplate simpMessagingTemplate, ChatRoomMemberRepository chatRoomMemberRepository) {
		this.messengerService = messengerService;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.chatRoomMemberRepository = chatRoomMemberRepository;
	}

	@GetMapping("/memberList")
	public String memberList(Model model) {
		
		List<MemberDeptRowDTO> memberList = messengerService.getMemberDeptRows();
		
		model.addAttribute("memberList", memberList);
		
		// 수정
		return "/messenger/memberList";
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
	}


	@GetMapping("/chatroom/{roomId}")
	public String chatroom(@PathVariable("roomId") Long roomId, 
	                       @AuthenticationPrincipal AccountLogin login, 
	                       Model model) {
	    Long myId = login.getId();

	    model.addAttribute("displayTitle", messengerService.getRoomDisplayTitle(roomId, myId));
	    
	    List<ChatMessageResponseDTO> chatHistory = messengerService.getChatHistory(roomId, myId); 
	    
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
        messengerService.updateLastReadAt(roomId, login.getId());
        
        // 실시간으로 '1'이 사라지게 하려면 여기서 웹소켓으로 "누가 읽었다"는 신호를 쏴줘야 합니다.
        ChatBroadcastMessageDTO readSignal = new ChatBroadcastMessageDTO();
        readSignal.setRoomId(roomId);
        readSignal.setMsgType("READ"); // 타입을 READ로 정의
        readSignal.setMemberId(login.getId());
        simpMessagingTemplate.convertAndSend("/topic/chatroom/" + roomId, readSignal);
        
        return "success";
    }
}










