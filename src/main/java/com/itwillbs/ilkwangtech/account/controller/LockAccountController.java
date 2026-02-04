package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.LoginAttemptDTO;
import com.itwillbs.ilkwangtech.account.service.LoginAttemptService;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class LockAccountController {

    private final LoginAttemptService loginAttemptService;

    @GetMapping("/lockAccount")
    public String getList() {
        return "account/lockAccount";
    }

    @GetMapping("/lockAccountList")
    @ResponseBody
    public Page<LoginAttemptDTO> getList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") Sort.Direction direction) {

        Page<LoginAttemptDTO> pageLoginAttemptDTO = loginAttemptService.getList(page, size, sortBy, direction);


        return pageLoginAttemptDTO;
    }

    @PostMapping("/unlock/{id}")
    public ResponseEntity<Void> unlock(@PathVariable Long id) {
        boolean result = loginAttemptService.unlock(id);
        return ResponseEntity.ok().build();
    }

}
