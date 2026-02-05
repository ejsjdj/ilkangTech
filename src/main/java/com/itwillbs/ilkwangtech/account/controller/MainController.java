package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {
	
	private final NoticeService noticeService;

    @GetMapping("/")
//    public String mainPage(@AuthenticationPrincipal AccountLogin accountLogin, Model model) {
    public String mainPage(@AuthenticationPrincipal AccountLogin accountLogin, Model model) {
    	log.info("메인 페이지 접속");
//        List<String> authorities = accountLogin.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//        for (String str : authorities) {
//            System.out.println("================================================================================");
//            System.out.println(str);
//        }
//        model.addAttribute("authorities", authorities);

    	model.addAttribute("pinnedNotices", noticeService.getPinnedNotices());
    	
        return "/schedule/calendar";
    }

}
