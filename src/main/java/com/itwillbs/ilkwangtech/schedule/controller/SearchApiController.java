package com.itwillbs.ilkwangtech.schedule.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.schedule.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Log4j2
public class SearchApiController {
	
	private final ScheduleService scheduleService;
	
	// 부서(팀) 검색 기능 구현
	@GetMapping("/team")
    public List<Department> searchTeam(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
		log.info("searchTeam() 실행!");
		log.info("searchTeam() 종료!");
        return scheduleService.searchTeams(keyword);
    }

	// 사원 검색 기능 구현
    @GetMapping("/member")
    public List<Member> searchMember(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
    	log.info("searchMember() 실행!");
		log.info("searchMember() 종료!");
        return scheduleService.searchMembers(keyword);
    }

}
