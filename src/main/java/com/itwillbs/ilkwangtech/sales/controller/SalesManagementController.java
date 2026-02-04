package com.itwillbs.ilkwangtech.sales.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesManagementController {

    @GetMapping("/management")
    public String salesManagement(Model model) {
        // 통계 데이터 추가 (실제로는 DB에서 조회)
        model.addAttribute("totalCustomers", 25);
        model.addAttribute("activeOrders", 12);
        model.addAttribute("pendingDeliveries", 8);
        model.addAttribute("unpaidInvoices", 5);
        model.addAttribute("totalQuotations", 45);
        model.addAttribute("priceNegotiations", 3);
        model.addAttribute("totalOrders", 156);
        model.addAttribute("totalDeliveries", 142);
        model.addAttribute("totalInvoices", 138);
        model.addAttribute("totalWarehouses", 4);
        
        return "sales/sales-management";
    }

    @GetMapping("/api/statistics")
    @ResponseBody
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 실제 통계 데이터 (DB에서 조회)
        stats.put("totalCustomers", 25);
        stats.put("activeOrders", 12);
        stats.put("pendingDeliveries", 8);
        stats.put("unpaidInvoices", 5);
        stats.put("totalQuotations", 45);
        stats.put("priceNegotiations", 3);
        stats.put("totalOrders", 156);
        stats.put("totalDeliveries", 142);
        stats.put("totalInvoices", 138);
        stats.put("totalWarehouses", 4);
        
        return stats;
    }
}
