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
	@Transactional(readOnly = true)
	public Page<ScheduleDTO> getScheduleList(Long loginMemberId, ScheduleSearchDTO params, int page) {
		
		LocalDateTime startDateTime;
	    LocalDateTime endDateTime;
        
        // 날짜 기본값 설정 (기존 코드 유지)
        if (params.getStartDate() == null) {
        	startDateTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            endDateTime = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        } else {
        	// 사용자가 '2026-01-16'을 선택했다면?
            // 시작: 2026-01-16 00:00:00
            startDateTime = params.getStartDate().atStartOfDay(); 
            
            // 끝: 2026-01-16 23:59:59.999999 (이래야 그날 일정 다 나옴)
            endDateTime = params.getEndDate().atTime(LocalTime.MAX);
        }

        // [추가] 페이징 객체 생성 (화면은 1부터 시작하므로 page-1 처리, 10개씩, 날짜순 정렬)
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("startDate").ascending());

        // 리포지토리 호출
        Page<Schedule> pageResult = scheduleRepository.findMyAndCompanySchedules(
                loginMemberId,
                startDateTime,
                endDateTime,
                params.getKeyword(),
                pageable
        );

        // Page<Entity> -> Page<DTO> 변환 (.map 사용)
        return pageResult.map(ScheduleDTO::fromEntity);
    }

}
