//package com.itwillbs.ilkwangtech.production.service;
//
//
//import com.itwillbs.ilkwangtech.production.dto.ProductionStatisticsDTO;
//import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProductionDashboardServiceImpl implements ProductionDashboardService{
//
//    private final ProductionPlaneRepository productionPlaneRepository;
//
//    @Override
//    @Transactional
//    public List<Object[]> getStatistics(String unit) {
//
//        LocalDate now = LocalDate.now();
//
//        LocalDateTime startDate = now.minusMonths(5)
//                .withDayOfMonth(1)
//                .atStartOfDay();
//
//        LocalDateTime endDate = now.plusMonths(1)
//                .withDayOfMonth(1)
//                .atStartOfDay();
//
//        if(unit.equals("DAY")) {
//            return productionPlaneRepository.getDailyProduction(startDate, endDate);
//        }
//
//        if(unit.equals("WEEK")) {
//            return productionPlaneRepository.getWeeklyProduction(startDate, endDate);
//        }
//
//        if(unit.equals("MONTH")) {
//            return productionPlaneRepository.getMonthlyProduction(startDate, endDate);
//        }
//
//        throw new IllegalArgumentException("Invalid unit");
//    }
//}
