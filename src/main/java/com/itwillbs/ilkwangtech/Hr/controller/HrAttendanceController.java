package com.itwillbs.ilkwangtech.Hr.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/attendance")
public class HrAttendanceController {
    // 외근 관리
    @GetMapping("/outwork/{memberId}")
    public String getOutworkManage(@PathVariable("memberId") Long id){
        return "외근 관리";
    }

    // 출퇴근 관리
    @GetMapping("/commute/{memberId}")
    public String getCommuteManage(@PathVariable("memberId") Long id){
        return "출퇴근 관리";
    }

    // 연차 관리
    @GetMapping("/vacation/{memberId}")
    public String getVacationManage(@PathVariable("memberId") Long id){
        return "연차 관리";
    }
}
