package com.itwillbs.ilkwangtech.Hr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController("/draft")
public class HrApprovalController {

    // 기안서 양식 선택 -> 작성 모달 요청
    // 전체결재(기본값), 결재대기, 결재완료
    @GetMapping("/List")
    public String getApprovalList(){

      return null;
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
