package com.itwillbs.ilkwangtech.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {
	
	// src/main/resources/testmain.html
	@GetMapping("/testmain")
	public String getMain() {
		return "testmain";
	}
	
	

}
