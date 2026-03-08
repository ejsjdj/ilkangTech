package com.itwillbs.ilkwangtech.inventorymg.controller;

import com.itwillbs.ilkwangtech.inventorymg.dto.InventoryHistoryDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.InventoryListDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.OutboundListDTO;
import com.itwillbs.ilkwangtech.inventorymg.service.InventoryListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventorymg")
public class InventoryListController {
	
    private final InventoryListService inventoryListService;

    // 화면 뷰 반환
    @GetMapping("/inventorylist")
    public String getMethodName() {
        return "inventorymg/inventorylist";
    }
	
    // Toast UI에서 호출할 데이터 API (수정된 부분)
    @GetMapping("/api/inventory-data")
    @ResponseBody
    public ResponseEntity<List<InventoryListDTO>> getInventoryData(
            @RequestParam(name = "tab", defaultValue = "ALL") String tab,
            @RequestParam(name = "searchType", required = false, defaultValue = "itemName") String searchType,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
        
        List<InventoryListDTO> data = inventoryListService.getFilteredInventoryList(tab, searchType, keyword);
        return ResponseEntity.ok(data);
    }
    
    // 실수량 조절 API
    @PreAuthorize("hasAnyAuthority('CEO', 'PRODUCTION', 'PURCHASING')")
    @PostMapping("/api/inventory/adjust")
    @ResponseBody
    public ResponseEntity<String> adjustInventory(
            @RequestParam("inventoryId") Long inventoryId,
            @RequestParam("type") String type,
            @RequestParam("qty") Long qty,
            @RequestParam("reason") String reason) {
        try {
            String result = inventoryListService.adjustInventory(inventoryId, type, qty, reason);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 폐기 처리 API
    @PreAuthorize("hasAnyAuthority('CEO', 'PRODUCTION', 'PURCHASING')")
    @PostMapping("/api/inventory/discard")
    @ResponseBody
    public ResponseEntity<String> discardInventory(
            @RequestParam("inventoryId") Long inventoryId,
            @RequestParam("qty") Long qty,
            @RequestParam("reason") String reason) {
        try {
            String result = inventoryListService.discardInventory(inventoryId, qty, reason);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 출고 목록 페이지 화면 연결
    @GetMapping("/outboundlist")
    public String outboundList() {
        return "inventorymg/outboundlist"; 
    }
    
    // 출고 목록 페이지 데이터 로드 API
    @GetMapping("/api/outbound-data")
    public ResponseEntity<List<OutboundListDTO>> getOutboundDataList(
            @RequestParam(name = "tab", defaultValue = "ALL") String tab,
            @RequestParam(name = "searchType", defaultValue = "itemName") String searchType,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
            
        // 서비스로 탭, 검색조건, 검색어 전달
        List<OutboundListDTO> resultList = inventoryListService.getOutboundListData(tab, searchType, keyword);
        
        return ResponseEntity.ok(resultList);
    }
    
    // 재고 이력 페이지 화면 연결
    @GetMapping("/inventoryhistory")
    public String inventoryHistory() {
        return "inventorymg/inventoryhistory"; 
    }
    
    // 재고 이력 데이터 로드 API
    @GetMapping("/api/inventory-history-data")
    @ResponseBody
    public ResponseEntity<List<InventoryHistoryDTO>> getInventoryHistoryData(
            @RequestParam(name = "tab", defaultValue = "ALL") String tab,
            @RequestParam(name = "searchType", defaultValue = "itemName") String searchType,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
            
        List<InventoryHistoryDTO> resultList = inventoryListService.getInventoryHistoryData(tab, searchType, keyword);
        return ResponseEntity.ok(resultList);
    }
}