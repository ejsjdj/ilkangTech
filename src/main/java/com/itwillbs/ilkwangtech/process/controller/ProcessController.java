package com.itwillbs.ilkwangtech.process.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/process")
@RequiredArgsConstructor
public class ProcessController {

	
	@GetMapping("/LOT")
    public String chaseLOTPage() {
        return "process/LOT"; 
    }
}
