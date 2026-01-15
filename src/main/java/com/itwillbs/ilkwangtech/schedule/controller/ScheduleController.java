package com.itwillbs.ilkwangtech.schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/schedule/*")
@Log4j2
public class ScheduleController {
	
	@GetMapping("/calendar")
    public String calendar() {
        return "/schedule/calendar";
    }
	
	@GetMapping("/list")
	public String scheduleListGET(Model model, HttpSession session) {
		log.info("scheduleListGET() 실행!");
		
		log.info("scheduleListGET() 종료!");
		return "/schedule/list";
	}
	

}
