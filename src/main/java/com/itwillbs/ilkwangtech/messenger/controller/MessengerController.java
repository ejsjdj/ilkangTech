package com.itwillbs.ilkwangtech.messenger.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.messenger.dto.ChatRoomListResponseDTO;
import com.itwillbs.ilkwangtech.messenger.dto.MemberDeptRowDTO;
import com.itwillbs.ilkwangtech.messenger.entity.ChatMessage;
import com.itwillbs.ilkwangtech.messenger.entity.ChatRoom;
import com.itwillbs.ilkwangtech.messenger.service.ChatMessageService;
import com.itwillbs.ilkwangtech.messenger.service.MessengerService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/messenger")
public class MessengerController {
	
	private final MessengerService messengerService;
	
	public MessengerController(MessengerService messengerService) {
		this.messengerService = messengerService;
	}

	@GetMapping("/memberList")
	public String memberList(Model model) {
		
		List<MemberDeptRowDTO> memberList = messengerService.getMemberDeptRows();
		
		model.addAttribute("memberList", memberList);
		
		// 수정
		return "/messenger/memberList";
	}
	
	@GetMapping("/chatList") // 또는 설정하신 URL 경로
	public String chatList(@AuthenticationPrincipal AccountLogin login, Model model) {
	    
	    // 1. 현재 로그인한 사용자의 ID로 방 목록 조회
	    List<ChatRoomListResponseDTO> rooms = messengerService.getChatRoomList(login.getId());
	    
	    // 2. 중요: HTML의 ${rooms}와 이름이 반드시 일치해야 합니다.
	    model.addAttribute("rooms", rooms); 
	    
	    // 3. 리턴하는 HTML 파일 경로가 정확한지 확인 (예: src/main/resources/templates/messenger/chatList.html)
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


	@GetMapping("/chatroom/{roomId}")
	public String chatroom(@PathVariable("roomId") Long roomId, 
	                       @AuthenticationPrincipal AccountLogin login, 
	                       Model model) {
	    Long myId = login.getId();

	    // 상단 타이틀용 이름과 과거 내역 조회
	    model.addAttribute("displayTitle", messengerService.getRoomDisplayTitle(roomId, myId));
	    model.addAttribute("chatHistory", messengerService.getChatHistory(roomId));
	    
	    model.addAttribute("roomId", roomId);
	    model.addAttribute("myMemberId", myId);

	    return "messenger/chatroom";
	}
	
	// [추가] 특정 방의 채팅 내역을 JSON으로 반환하는 API
    @GetMapping("/api/chat/{roomId}")
    @ResponseBody
    public List<ChatMessage> getChatHistoryApi(@PathVariable("roomId") Long roomId) {
        return messengerService.getChatHistory(roomId);
    }


}










