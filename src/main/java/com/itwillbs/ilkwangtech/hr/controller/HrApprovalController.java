package com.itwillbs.ilkwangtech.hr.controller;

import com.itwillbs.ilkwangtech.hr.dto.*;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.hr.service.*;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/draft")
@RequiredArgsConstructor
@Log4j2
public class HrApprovalController {

    private final DraftRepository draftRepository;
    private final DraftRegistService draftRegistService;
    private final DraftService draftService;
    private final DraftApprovalLineService draftApprovalLineService;
    private final DraftDetailService draftDetailService;
    private final DraftDecideService draftDecideService;

    // 결재문서 리스트
    @GetMapping("/list")
    public String getApprovalList(@AuthenticationPrincipal AccountLogin accountLogin,Model model){
        Long userId = accountLogin.getId();
        List<DraftDTO> draftList = draftService.getDraftById(userId);

        model.addAttribute("draftList", draftList);
        return "/hr/draft";
    }

    // 결재문서 상세보기
    @GetMapping("/detail")
    @ResponseBody
    public DraftDetailDTO getApprovalDetail(@RequestParam("draftId") long draftId,
                                            @AuthenticationPrincipal AccountLogin accountLogin){
        System.out.println("상세보기 문서 Id : " + draftId);

        Long userId = accountLogin.getId();

        List<String> roleList = accountLogin.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        return draftDetailService.getDraftDetail(userId, draftId, roleList);
    }

    // 결재문서 양식 선택
    @GetMapping("/type")
    @ResponseBody
    public List<DraftApprovalLineDTO> getApprovalStatus(
            @RequestParam(value = "type", required = false) String draftType
    ){
        return draftApprovalLineService.getLineByType(draftType);
    }

    // 결재 등록
    // 결재 문서 테이블에 정보 저장
    @PostMapping("/register")
    @ResponseBody
    public void createApproval(@RequestBody DraftRegistDTO draftRegistDTO,
                               @AuthenticationPrincipal AccountLogin accountLogin){

        draftRegistService.putDraft(draftRegistDTO, accountLogin.getId());

    }

    // 결재 승인/반려
    @PutMapping("/decide")
    public String approvalDecide(@RequestBody Map<String, Object> payload,
                               @AuthenticationPrincipal AccountLogin accountLogin){
    	log.info("approvalDecidePOST() 실행!");
        Long userId = accountLogin.getId();

        long draftId = Long.parseLong(payload.get("draftId").toString());
        String decision = (String) payload.get("decision");

        System.out.println("draftId : " + draftId + " decision : " +  decision + "userId : " + userId);
        // 서비스 호출 (최종 승인 시 캘린더 등록 로직 포함됨)
        try {
            draftDecideService.putApprovalDecide(userId, draftId, decision);
            return "success"; // AJAX 호출에 대한 응답
        } catch (Exception e) {
            log.error("결재 처리 중 오류 발생", e);
            return "fail";
        }
    }
}
