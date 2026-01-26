package com.itwillbs.ilkwangtech.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;


@Controller
@RequestMapping("/notice/*")
@Log4j2
public class NoticeController {
	
	@GetMapping("/list")
	public String getNoticeList() {
		log.info("getNoticeList() 실행!");
		log.info("getNoticeList() 끝!");
		return "/notice/list";
	}
	

}
