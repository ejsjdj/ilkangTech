package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.dto.DraftApprovalLineDTO;
import com.itwillbs.ilkwangtech.Hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.Hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.Hr.service.DraftApprovalLineService;
import com.itwillbs.ilkwangtech.Hr.service.DraftRegistService;
import com.itwillbs.ilkwangtech.Hr.service.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/draft")
@RequiredArgsConstructor
public class HrApprovalController {

    private final DraftRepository draftRepository;
    private final DraftRegistService draftRegistService;
    private final DraftService draftService;
    private final DraftApprovalLineService draftApprovalLineService;

    // 기안서 양식 선택 -> 작성 모달 요청
    // 전체결재(기본값), 결재대기, 결재완료
    @GetMapping("/list/{id}")
    public String getApprovalList(@PathVariable long id, Model model){
        List<DraftDTO> draftList = draftService.getDraftById(id);
        model.addAttribute("draftList", draftList);
        return "/hr/draft";
    }

    // 기안서 작성 중 양식 선택
    @GetMapping("/type")
    public String getDraftStatus(
            @RequestParam(value = "type", required = false) String draftStatus,
            Model model
    ){
        List<DraftApprovalLineDTO> approvalLine = draftApprovalLineService.getLineByType(draftStatus);
        model.addAttribute("approvalLine", approvalLine);

        return "/hr/approvalLineTest";
    }

    // 결재 등록
    // 결재 문서 테이블에 정보 저장
    @PostMapping("/approver")
    public void createApproval(@ModelAttribute DraftRegistDTO draftRegistDTO){
        // 결재 문서 타입 --> 결재 문서 테이블에 저장
        // 결재 문서 제목 --> 결재 문서 테이블에 저장
        // 결재 문서 내용 --> 결재 문서 테이블에 저장
        // 결재 문서 시작일 --> 결재 문서 테이블에 저장
        // 결재 문서 만료일 --> 결재 문서 테이블에 저장
        // 결재 문서 첨부파일 --> 결재 문서 테이블에 저장

        // 결재 문서 id --> 결재 상태 테이블에 저장
        // 결재자 id --> 결재 상태 테이블에 저장
        // 결재 순서 --> 결재 상태 테이블에 저장

        // 결재 상태 --> 결재 문서 테이블, 결재 상태 테이블(1차 결재자 결재 상태 컬럼)에 저장

        draftRegistService.putDraft(draftRegistDTO);
    }

    // 결재 승인/반려
}
