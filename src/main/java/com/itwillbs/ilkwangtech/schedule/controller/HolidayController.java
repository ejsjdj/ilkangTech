package com.itwillbs.ilkwangtech.schedule.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.ilkwangtech.schedule.service.HolidayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class HolidayController {
    
    private final HolidayService holidayService;

    @GetMapping("/holidays")
    public List<Map<String, Object>> getHolidays(@RequestParam(name = "year") String year) {
        return holidayService.getHolidays(year);
    }
}
