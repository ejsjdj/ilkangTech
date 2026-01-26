package com.itwillbs.ilkwangtech.notice.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Controller
@RequestMapping("/notice/*")
@Log4j2
@RequiredArgsConstructor
public class NoticeController {
	
	private final NoticeService noticeService;
	
	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "0") int page, Model model, 
	                   @AuthenticationPrincipal AccountLogin loginMember) {
	    
	    // 고정 게시글 및 일반 리스트 조회
	    model.addAttribute("pinnedList", noticeService.getPinnedNotices());
	    model.addAttribute("noticeList", noticeService.getNoticeList(page));

	    // 1-4. 권한 체크: 전산관리부 혹은 관리부 계정 확인
	    boolean canWrite = false;
	    if (loginMember != null) {
	        // AccountLogin 객체의 department 필드를 직접 참조
	        String dept = loginMember.getDepartment(); 
	        
	        if ("전산관리부".equals(dept) || "관리부".equals(dept)) {
	            canWrite = true;
	        }
	        
	        log.info("접속 사원: {}, 부서: {}, 쓰기권한: {}", loginMember.getName(), dept, canWrite);
	    }
	    model.addAttribute("canWrite", canWrite);

	    return "notice/list";
	}
	

}
