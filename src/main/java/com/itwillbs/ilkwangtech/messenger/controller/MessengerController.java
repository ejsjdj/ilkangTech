package com.itwillbs.ilkwangtech.messenger.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.ilkwangtech.messenger.dto.MemberDeptRowDTO;
import com.itwillbs.ilkwangtech.messenger.service.MessengerService;
import org.springframework.web.bind.annotation.RequestParam;


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
	
	@GetMapping("/chatList")
	public String chatList(Model model) {
		return "/messenger/chatList";
	}
	
	
}
