package com.itwillbs.ilkwangtech.Hr.controller;

import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/draft")
@RequiredArgsConstructor
public class HrApprovalController {

    private final DraftRepository draftRepository;

    // 기안서 양식 선택 -> 작성 모달 요청
    // 전체결재(기본값), 결재대기, 결재완료
    @GetMapping("/list")
    public List<DraftEntity> getApprovalList(){
      return draftRepository.findAll();
    }

    // 기안서 작성 중 양식 선택
    @GetMapping("/status")
    public String getDraftStatus(
            @RequestParam(value = "draftStatus", required = false) String draftStatus
    ){
        return null;
    }

    // 결재자 선택
    @GetMapping("/approver")
    public String getApprover(){
        return null;
    }
}
