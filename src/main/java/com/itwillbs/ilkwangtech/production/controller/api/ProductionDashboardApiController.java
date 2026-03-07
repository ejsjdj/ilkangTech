package com.itwillbs.ilkwangtech.production.controller.api;

import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
import com.itwillbs.ilkwangtech.production.service.ProductionDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/production_dashboard")
@RequiredArgsConstructor
@Log4j2
public class ProductionDashboardApiController {

    private final ProductionPlaneRepository productionPlaneRepository;
    private final ProductionDashboardService productionDashboardService;

    // 금일 생산 계획
    @GetMapping("/today_plane")
    public Long getTodayPlane(){
        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return productionPlaneRepository.findTodayPlans(start, end);
    }

    // 금일 완료 계획
    @GetMapping("/today_complete_plane")
    public Long getTodayCompletePlane(){
        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return productionPlaneRepository.findCompletePlans(start, end);
    }

    // 진행중인 생산계획
    @GetMapping("/processing_plane")
    public Long getProcessingPlane(){
        return productionPlaneRepository.findProgressPlans();
    }

    // 지연 생산계획
    @GetMapping("/waiting_plane")
    public Long getWaitingPlane(){
        return productionPlaneRepository.findWaitingPlans();
    }

    // 월별 계획수량
    @GetMapping("/date_by_plane")
    public List<Object[]> getByDatePlaningQty(@RequestParam(value = "unit") String unit) {
        return productionDashboardService.getStatistics(unit);
    }

    // 품목별 생산수량
    @GetMapping("/get_by_item")
    public List<Object[]> getByItemQty(){
        return productionPlaneRepository.findByItemQty();
    }
}
