package com.itwillbs.ilkwangtech.layout;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layout/*")
public class layoutController {

	@GetMapping("/layout")
    public String layout() {
        return "/layout/layout";
    }
	
}
