package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.DraftApprovalLineDTO;
import com.itwillbs.ilkwangtech.Hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.Hr.dto.DraftDetailDTO;
import com.itwillbs.ilkwangtech.Hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.Hr.service.*;
import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/draft")
@RequiredArgsConstructor
public class HrApprovalController {

    private final DraftRepository draftRepository;
    private final DraftRegistService draftRegistService;
    private final DraftService draftService;
    private final DraftApprovalLineService draftApprovalLineService;
    private final DraftDetailService draftDetailService;
    private final DraftDecideService draftDecideService;

    // 결재문서 양식 선택 -> 결재문서 작성 모달 요청
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
    public DraftDetailDTO getApprovalDetail(@RequestParam("draftId") long draftId, @AuthenticationPrincipal AccountLogin accountLogin){
        System.out.println("상세보기 문서 Id : " + draftId);
        Long userId = accountLogin.getId();
        return draftDetailService.getDraftDetail(userId, draftId);
    }

    // 결재문서 작성 중 양식 선택
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
    public void createApproval(@RequestBody DraftRegistDTO draftRegistDTO, @AuthenticationPrincipal AccountLogin accountLogin){
        Long userId = accountLogin.getId();

        draftRegistService.putDraft(draftRegistDTO, userId);
    }

    // 결재 승인/반려
    @PutMapping("/decide")
    public String approvalDecide(@RequestBody Map<String, Object> payload,
                               @AuthenticationPrincipal AccountLogin accountLogin){
        Long userId = accountLogin.getId();

        long draftId = Long.parseLong(payload.get("draftId").toString());
        String decision = (String) payload.get("decision");

        System.out.println("draftId : " + draftId + " decision : " +  decision + "userId : " + userId);
        draftDecideService.putApprovalDecide(userId, draftId, decision);

        return "redirect:/draft/list";
    }
}
