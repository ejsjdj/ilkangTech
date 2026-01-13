package com.itwillbs.ilkwangtech.Hr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/approval")
public class HrApprovalController {



    //    기안서 작성 모달 요청
//    전체결재(기본값), 결재대기, 결재완료
//    @GetMapping("/list")
//    public void getMember(
//      @RequestParam(required = false, defaultValue = "ALL") String status
//      @RequestParam(required = false) String title
//      @RequestParam(required = false) LocalDate startDate
//      @RequestParam(required = false) LocalDate endDate
//    ){
//
//      return null;
//
//    }
//
//    기안서 작성
//    @GetMapping("/commuteManage")
//    public void getAppointment(@RequestParam(defaultValue = "vacation") String status){
//
//    }
//
//    결재자 선택
//    @GetMapping("/commuteManage")
//    public void getAppointment(@RequestParam(defaultValue = "vacation") String status){
//
//    }
}
