package com.itwillbs.ilkwangtech.inventorymg.controller;

import com.itwillbs.ilkwangtech.inventorymg.dto.ChartDataDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.InboundItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.OutboundItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.RackItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.service.DashboardService;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderRepository;

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
        
        // 더미 데이터 대신 실제 DB 값을 호출하도록 수정!
        model.addAttribute("inboundScheduled", dashboardService.getInboundScheduledCount());
        // 금일 입고 처리 완료 건수
        model.addAttribute("inboundProcessed", dashboardService.getInboundProcessedToday());
        
        // 출고 지시는 생산/영업팀 데이터로 나중에 교체
        model.addAttribute("outboundOrdered", 3); 
        // 금일 출고 처리 완료 건수
        model.addAttribute("outboundProcessed", dashboardService.getOutboundProcessedToday());
        
//        model.addAttribute("imminentCount", dashboardService.getImminentStockCount());
        
        // 창고 상태 & 발주 필요 리스트
        model.addAttribute("warehouse", dashboardService.getWarehouseStatus());
        model.addAttribute("orderList", dashboardService.getOrderNeededList());
        
        return "inventorymg/dashboard"; 
    }

    // 랙 상세 정보 모달용 AJAX 통신 API
    @GetMapping("/rack-info")
    @ResponseBody
    public ResponseEntity<List<RackItemDTO>> getRackInfo(
            @RequestParam("zone") String zone, 
            @RequestParam("rack") String rack) {
        
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
    
    // 새로운 POST API 추가 (맨 아래에 추가)
    @PostMapping("/transfer")
    @ResponseBody
    public ResponseEntity<String> transferInventory(
            @RequestParam("inventoryId") Long inventoryId,
            @RequestParam("targetZone") String targetZone,
            @RequestParam("targetRack") String targetRack,
            @RequestParam("transferQty") Long transferQty) {
        
        dashboardService.transferInventory(inventoryId, targetZone, targetRack, transferQty);
        return ResponseEntity.ok("재고 이동이 완료되었습니다.");
    }
    
    // 금일 입고 예정 상세 리스트 모달용 API
    @GetMapping("/inbound-scheduled-list")
    @ResponseBody
    public ResponseEntity<List<InboundItemDTO>> getInboundScheduledList() {
        return ResponseEntity.ok(dashboardService.getInboundScheduledList());
    }
    
    // 차트 데이터 AJAX 요청 API
    @GetMapping("/chart-data")
    @ResponseBody
    public ResponseEntity<ChartDataDTO> getChartData(@RequestParam(name = "type", defaultValue = "month") String type) {
        return ResponseEntity.ok(dashboardService.getChartData(type));
    }
    
    // 발주(구매 요청) AJAX 처리
    @PostMapping("/purchase-request")
    @ResponseBody
    public String purchaseRequest(@RequestBody List<Map<String, Long>> requestData) {
        dashboardService.createPurchaseRequests(requestData);
        return "발주 요청이 완료되었습니다.";
    }
    
    // 금일 출고 대기 리스트 조회
    @GetMapping("/outbound-scheduled-list")
    @ResponseBody
    public ResponseEntity<List<OutboundItemDTO>> getOutboundScheduledList() {
        return ResponseEntity.ok(dashboardService.getOutboundScheduledList());
    }

    // 금일 출고 처리 실행
    @PostMapping("/process-outbound")
    @ResponseBody
    public ResponseEntity<String> processOutbound() {
        try {
            String result = dashboardService.processOutbound();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}