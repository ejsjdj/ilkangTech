package com.itwillbs.ilkwangtech.schedule.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.schedule.dto.ScheduleDTO;
import com.itwillbs.ilkwangtech.schedule.dto.ScheduleSearchDTO;
import com.itwillbs.ilkwangtech.schedule.entity.Schedule;
import com.itwillbs.ilkwangtech.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ScheduleService {
	
	private final ScheduleRepository scheduleRepository;
	
	// 일정 리스트 조회
	// [변경] 파라미터가 int page -> Pageable pageable 로 바뀝니다.
	@Transactional(readOnly = true)
	public Page<ScheduleDTO> getScheduleList(Long loginMemberId, ScheduleSearchDTO params, Pageable pageable) {
	    
	    LocalDateTime startDateTime;
	    LocalDateTime endDateTime;
	    
	    // 1. 날짜 기본값 설정 
	    if (params.getStartDate() == null) {
	        startDateTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
	        endDateTime = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
	    } else {
	        startDateTime = params.getStartDate().atStartOfDay(); 
	        endDateTime = params.getEndDate().atTime(LocalTime.MAX);
	    }
	    
	    // 검색 카테고리 종류(공유 범위) 부분 추가
	    String dbKeyword = params.getKeyword();
	    
	    if ("type".equals(params.getSearchType()) && dbKeyword != null) {
	        String k = dbKeyword.trim();
	        if ("회사".equals(k)) {
	            dbKeyword = "COMPANY"; // 임시 변수만 변경
	        } else if ("개인".equals(k)) {
	            dbKeyword = "PERSONAL"; // 임시 변수만 변경
	        }
	    }

	    // Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("startDate").ascending());
	    
	    // 2. 리포지토리 호출
	    Page<Schedule> pageResult = scheduleRepository.findMyAndCompanySchedules(
	            loginMemberId,
	            startDateTime,
	            endDateTime,
	            dbKeyword,
	            params.getSearchType(), // [추가] DTO에서 꺼내서 전달
	            pageable 
	    );

	    // 3. 변환 후 반환
	    return pageResult.map(ScheduleDTO::fromEntity);
	}


}
