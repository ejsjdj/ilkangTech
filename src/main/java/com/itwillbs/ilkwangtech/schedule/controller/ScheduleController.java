package com.itwillbs.ilkwangtech.schedule.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.ilkwangtech.schedule.dto.ScheduleDTO;
import com.itwillbs.ilkwangtech.schedule.dto.ScheduleSearchDTO;
import com.itwillbs.ilkwangtech.schedule.service.ScheduleService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule/*")
@Log4j2
public class ScheduleController {

	private final ScheduleService scheduleService;
	
	// [추가 1] 빈 문자열("")을 null로 변환하여 LocalDate 바인딩 에러 방지
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

	@GetMapping("/calendar")
	public String calendarGET() {
		log.info("calendarGET() 실행!");
		log.info("calendarGET() 실행!");
		return "/schedule/calendar";
	}

	@GetMapping("/list")
	public String scheduleListGET(Model model, HttpSession session,
								  @ModelAttribute("searchParams") ScheduleSearchDTO params,
								  @RequestParam(name = "page", defaultValue = "1") int page,
								  @RequestParam(name = "sortField", defaultValue = "startDate") String sortField,
	                              @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) { // 페이지 번호 받기

		// 1. 로그인 체크 (기존 코드 유지)
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AccountLogin loginMember = (AccountLogin) authentication.getPrincipal();
		if (loginMember == null) return "redirect:/account/login";
		
		// 정렬 생성
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
	    Pageable pageable = PageRequest.of(page - 1, 10, sort);

		// 2. 서비스 호출
		Page<ScheduleDTO> result = scheduleService.getScheduleList(loginMember.getId(), params, pageable);

		// 3. 모델에 담기
		model.addAttribute("schedules", result); // Page 객체 자체를 넘김

		// [추가] 페이지네이션 UI 계산 (보여줄 페이지 번호 범위)
		// 현재 페이지를 기준으로 앞뒤 5페이지씩, 총 10페이지 표시
		int blockLimit = 10;
		int startPage = (((int)(Math.ceil((double)page / blockLimit))) - 1) * blockLimit + 1;
		int endPage = Math.min((startPage + blockLimit - 1), result.getTotalPages());

		// 데이터가 아예 없을 때 endPage가 0이 되는 것을 방지
		if (endPage == 0) endPage = 1;

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("currentPage", page);
		
		model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

		return "/schedule/list";
	}


}
