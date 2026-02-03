package com.itwillbs.ilkwangtech.sales.controller;

import com.itwillbs.ilkwangtech.sales.dto.ItemStockDTO;
import com.itwillbs.ilkwangtech.sales.dto.PriceNegotiationDTO;
import com.itwillbs.ilkwangtech.sales.dto.ProductPriceHistoryDTO;
import com.itwillbs.ilkwangtech.sales.dto.QuotationDTO;
import com.itwillbs.ilkwangtech.sales.service.PriceManagementService;
import com.itwillbs.ilkwangtech.sales.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sales/quotation")
@RequiredArgsConstructor
/**
 * 판매처와 제품의 단가를 정리하는 컨트롤러
 */
public class QuotationController {

    private final QuotationService quotationService;
    private final PriceManagementService priceManagementService;

    @PostMapping
    public ResponseEntity<QuotationDTO> createQuotation(@RequestBody QuotationDTO quotationDTO) {
        return ResponseEntity.ok(quotationService.createQuotation(quotationDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationDTO> getQuotation(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getQuotationById(id));
    }

    @GetMapping
    public ResponseEntity<List<QuotationDTO>> getAllQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<QuotationDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(quotationService.updateQuotationStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
        quotationService.deleteQuotation(id);
        return ResponseEntity.noContent().build();
    }

    // ===== 단가 관리 기능 =====
    // 제품 생성 시 초기 단가 설정
    @PostMapping("/item-with-price")
    public ResponseEntity<ItemStockDTO> createItemStockWithPrice(@RequestBody ItemStockDTO itemStockDTO) {
        ItemStockDTO createdItem = priceManagementService.createItemStockWithPrice(itemStockDTO);
        return ResponseEntity.ok(createdItem);
    }

    // 제품 기본 단가 수정
    @PutMapping("/product/{itemStockId}/price")
    public ResponseEntity<ItemStockDTO> updateProductPrice(
            @PathVariable Long itemStockId,
            @RequestParam BigDecimal newPrice,
            @RequestParam String changeReason,
            @RequestParam String registeredBy) {
        ItemStockDTO updatedItem = priceManagementService.updateProductPrice(itemStockId, newPrice, changeReason, registeredBy);
        return ResponseEntity.ok(updatedItem);
    }

    // 고객사별 단가 협상
    @PostMapping("/negotiate-price")
    public ResponseEntity<ProductPriceHistoryDTO> negotiateCustomerPrice(@RequestBody PriceNegotiationDTO negotiationDTO) {
        ProductPriceHistoryDTO priceHistory = priceManagementService.negotiateCustomerPrice(negotiationDTO);
        return ResponseEntity.ok(priceHistory);
    }

    // 현재 유효한 단가 조회
    @GetMapping("/current-price")
    public ResponseEntity<BigDecimal> getCurrentPrice(
            @RequestParam Long itemStockId,
            @RequestParam(required = false) Long customerId) {
        BigDecimal currentPrice = priceManagementService.getCurrentPrice(itemStockId, customerId);
        return ResponseEntity.ok(currentPrice);
    }

    // 제품 단가 이력 조회
    @GetMapping("/product/{itemStockId}/price-history")
    public ResponseEntity<List<ProductPriceHistoryDTO>> getPriceHistory(@PathVariable Long itemStockId) {
        List<ProductPriceHistoryDTO> history = priceManagementService.getPriceHistory(itemStockId);
        return ResponseEntity.ok(history);
    }

    // 모든 현재 유효한 단가 목록 조회
    @GetMapping("/current-prices")
    public ResponseEntity<List<ProductPriceHistoryDTO>> getAllCurrentPrices() {
        List<ProductPriceHistoryDTO> currentPrices = priceManagementService.getAllCurrentPrices();
        return ResponseEntity.ok(currentPrices);
    }
}
