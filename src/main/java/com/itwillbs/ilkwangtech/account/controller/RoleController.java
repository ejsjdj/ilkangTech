package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.MemberRoleViewDTO;
import com.itwillbs.ilkwangtech.account.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 권한 관리를 담당하는 컨트롤러
 * 권한별 직원 조회, 권한 부여 및 회수 기능을 제공합니다.
 */
@Controller
@RequestMapping("/account")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 권한 관리 메인 페이지를 요청합니다.
     * 전체 권한 목록과 권한을 부여할 수 있는 전체 직원 목록을 모델에 담아 전달합니다.
     *
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 권한 관리 페이지 뷰 경로
     */
    @GetMapping("/role")
    public String viewRolePage (Model model) {

        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("assignableMembers", roleService.getAssignableMembers());

        return "/account/role";
    }

    /**
     * 특정 권한을 가진 직원 목록을 조회합니다. (페이징 및 정렬 지원)
     *
     * @param roleId 조회할 권한 ID
     * @param page 페이지 번호 (기본값 0)
     * @param size 한 페이지당 출력할 데이터 수 (기본값 10)
     * @param sortBy 정렬 기준 필드 (기본값 memberId)
     * @param direction 정렬 방향 (ASC/DESC, 기본값 ASC)
     * @return 직원 권한 정보 목록이 담긴 Page 객체
     */

    @GetMapping("/findMemberByRole")
    public ResponseEntity<Page<MemberRoleViewDTO>> findAccountByRole(
            @RequestParam("roleId") long roleId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "memberId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {



        Sort.Direction sortDir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        
        Page<MemberRoleViewDTO> memberRoleViews = roleService.findMemberByRole(roleId, pageable);
        return ResponseEntity.ok(memberRoleViews);
    }

    /**
     * 특정 직원에게서 권한을 회수합니다.
     *
     * @param memberId 직원 고유 ID
     * @param roleId 회수할 권한 ID
     * @return 성공 시 200 OK
     */

    @DeleteMapping("/revokeRole")
    public ResponseEntity<Void> revokeRole(
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestBody(required = false) java.util.Map<String, Long> params) {
        
        if (memberId == null && params != null) memberId = params.get("memberId");
        if (roleId == null && params != null) roleId = params.get("roleId");

        roleService.revokeRole(memberId, roleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 직원에게 새로운 권한을 부여합니다.
     *
     * @param memberId 직원 고유 ID
     * @param roleId 부여할 권한 ID
     * @return 성공 시 200 OK
     */
    @PostMapping("/grantRole")
    public ResponseEntity<Void> grantRole(
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestBody(required = false) java.util.Map<String, Long> params) {
        
        if (memberId == null && params != null) memberId = params.get("memberId");
        if (roleId == null && params != null) roleId = params.get("roleId");

        roleService.grantRole(memberId, roleId);
        return ResponseEntity.ok().build();
    }

}
