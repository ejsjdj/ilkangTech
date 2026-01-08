package com.itwillbs.ilkwangtech.account.controller;

import ch.qos.logback.core.model.Model;
import com.itwillbs.ilkwangtech.account.dto.AccountDTO;
import com.itwillbs.ilkwangtech.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/join")
    public String join(@Valid @RequestBody AccountDTO.AccountJoinRequest req,
                       Model model,
                       RedirectAttributes redirectAttributes) {

            AccountDTO.AccountJoinResponse res = accountService.join(req);

        return "index.html";
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountDTO.AccountLoginRequest req) {

        return "index.html";
    }

}
