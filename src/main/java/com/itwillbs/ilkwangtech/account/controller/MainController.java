package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {
	
	private final NoticeService noticeService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal AccountLogin accountLogin, Model model) {
    	log.info("메인 페이지 접속");

    	model.addAttribute("pinnedNotices", noticeService.getPinnedNotices());
    	
        return "/schedule/calendar";
    }

}
