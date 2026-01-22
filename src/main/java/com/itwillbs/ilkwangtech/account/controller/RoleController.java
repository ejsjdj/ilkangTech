package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.MemberRoleView;
import com.itwillbs.ilkwangtech.account.repository.CommonCodeRepository;
import com.itwillbs.ilkwangtech.account.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@Controller
@RequestMapping("/account")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/role")
    public String viewRolePage (Model model) {

        model.addAttribute("roles", roleService.getRoles());

        return "/account/role";
    }

    @GetMapping("/findMemberByRole")
    public ResponseEntity<List<MemberRoleView>> findAccountByRole(@RequestParam("roleId") long roleId) {
        List<MemberRoleView> memberRoleViews = roleService.findMemberByRole(roleId);
        return ResponseEntity.ok(memberRoleViews);
    }

}
