package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @GetMapping("/")
//    public String mainPage(@AuthenticationPrincipal AccountLogin accountLogin, Model model) {
    public String mainPage(@AuthenticationPrincipal AccountLogin accountLogin) {
//        List<String> authorities = accountLogin.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//        for (String str : authorities) {
//            System.out.println("================================================================================");
//            System.out.println(str);
//        }
//        model.addAttribute("authorities", authorities);

        return "/schedule/calendar";
    }

}
