package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// 윤휘명 테스트
@Controller
public class AccountController {

    @PostMapping("/account/join")
    public ResponseEntity<?> join(@RequestBody AccountDTO.JoinRequest joinRequest) {
        return null;
    }

}
