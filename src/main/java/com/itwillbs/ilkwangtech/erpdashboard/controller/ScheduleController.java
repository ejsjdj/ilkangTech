package com.itwillbs.ilkwangtech.erpdashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/schedule/*")
public class ScheduleController {
	
	@GetMapping("/list")
	public String getMethodName(Model model, HttpSession session) {
		return new String();
	}
	

}
