package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.MemberRoleView;
import com.itwillbs.ilkwangtech.account.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<Page<MemberRoleView>> findAccountByRole(
            @RequestParam("roleId") long roleId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "memberId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {
        
        Sort.Direction sortDir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        
        Page<MemberRoleView> memberRoleViews = roleService.findMemberByRole(roleId, pageable);
        return ResponseEntity.ok(memberRoleViews);
    }

}
