package com.itwillbs.ilkwangtech.layout;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class layoutController {

	@GetMapping("/layout/layout")
    public String layout() {
		System.out.println("test");
        return "/layout/layout";
    }
	
}
