package com.itwillbs.ilkwangtech.inventorymg.controller;

import com.itwillbs.ilkwangtech.inventorymg.dto.InventoryListDTO;
import com.itwillbs.ilkwangtech.inventorymg.service.InventoryListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}