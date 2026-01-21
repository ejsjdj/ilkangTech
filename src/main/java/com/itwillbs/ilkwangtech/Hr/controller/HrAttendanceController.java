package com.itwillbs.ilkwangtech.Hr.controller;


import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/attendance")
public class HrAttendanceController {

    // 휴가 조회
    @GetMapping("/vacation/{memberId}")
    public String getVacationManage(@AuthenticationPrincipal AccountLogin accountLogin){

        /***
         * TODO :: Service에서 role별로 분기하여 필터링 후 조회
         * 1. 일반 사원 : 개인 휴가 조회
         * 2. 부서장 : 개인휴가 + 부서원 휴가
         * 3. 인사부장 + 임원진 : 개인휴가 + 전사원 휴가
        ***/

        return "연차 관리";
    }
}
