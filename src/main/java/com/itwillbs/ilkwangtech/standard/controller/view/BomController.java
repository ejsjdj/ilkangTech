package com.itwillbs.ilkwangtech.standard.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bom")
public class BomController {

    @GetMapping("/list")
    public String getBomList() {
        return "standard/bomList";
    }

}
