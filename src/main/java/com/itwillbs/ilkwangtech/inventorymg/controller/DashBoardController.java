package com.itwillbs.ilkwangtech.inventorymg.controller;

import com.itwillbs.ilkwangtech.inventorymg.dto.RackItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inventorymg/*")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        
        model.addAttribute("inboundScheduled", dashboardService.getInboundScheduledCount()); // 상단 현황판 더미 데이터 (입고예정, 출고지시, 발주필요 등)
        model.addAttribute("inboundProcessed", dashboardService.getInboundProcessedToday()); // 금일 입고 처리 완료 건수
        
        model.addAttribute("outboundOrdered", 3);
        model.addAttribute("outboundProcessed", dashboardService.getOutboundProcessedToday()); // 금일 출고 처리 완료 건수
        
        // 창고 상태
        model.addAttribute("warehouse", dashboardService.getWarehouseStatus());
        
        // 발주 필요 리스트
        model.addAttribute("orderList", dashboardService.getOrderNeededList());
        
        return "inventorymg/dashboard"; // 타임리프 경로 반환
    }

    // 발주(구매 요청) AJAX 처리
    @PostMapping("/purchase-request")
    @ResponseBody
    public ResponseEntity<String> requestPurchase(@RequestBody Map<String, List<Long>> payload) {
        List<Long> itemIds = payload.get("itemIds");
        dashboardService.createPurchaseRequests(itemIds);
        return ResponseEntity.ok("발주 요청(구매팀 전달)이 완료되었습니다.");
    }
    
    // 랙 상세 정보 모달용 AJAX 통신 API
    @GetMapping("/rack-info")
    @ResponseBody // 화면 이동 없이 JSON 데이터만 반환
    public ResponseEntity<List<RackItemDTO>> getRackInfo(@RequestParam("zone") String zone, @RequestParam("rack") String rack) {
        List<RackItemDTO> rackDetails = dashboardService.getRackDetails(zone, rack);
        return ResponseEntity.ok(rackDetails);
    }
    
    // 금일 입고 예정 항목(COMPLETE)들을 실제 창고로 입고 처리하는 API
    @PostMapping("/process-inbound")
    @ResponseBody
    public ResponseEntity<String> processInbound() {
        dashboardService.processCompleteOrders();
        return ResponseEntity.ok("입고 대기(COMPLETE) 항목들이 창고 ABC에 성공적으로 분배 및 입고되었습니다.");
    }
}