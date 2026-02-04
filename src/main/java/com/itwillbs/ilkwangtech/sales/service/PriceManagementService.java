package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.ItemStockDTO;
import com.itwillbs.ilkwangtech.sales.dto.PriceNegotiationDTO;
import com.itwillbs.ilkwangtech.sales.dto.ProductPriceHistoryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PriceManagementService {
    
    // 제품 생성 시 초기 단가 설정
    ItemStockDTO createItemStockWithPrice(ItemStockDTO itemStockDTO);
    
    // 제품 단가 수정 (기본 단가)
    ItemStockDTO updateProductPrice(Long itemStockId, BigDecimal newPrice, String changeReason, String registeredBy);
    
    // 고객사별 단가 협상
    ProductPriceHistoryDTO negotiateCustomerPrice(PriceNegotiationDTO negotiationDTO);
    
    // 현재 유효한 단가 조회
    BigDecimal getCurrentPrice(Long itemStockId, Long customerId);
    
    // 제품 단가 이력 조회
    List<ProductPriceHistoryDTO> getPriceHistory(Long itemStockId);
    
    // 고객사별 제품 단가 이력 조회
    List<ProductPriceHistoryDTO> getCustomerPriceHistory(Long customerId, Long itemStockId);
    
    // 모든 현재 유효한 단가 목록 조회
    List<ProductPriceHistoryDTO> getAllCurrentPrices();
    
    // 특정 기간 동안의 단가 변경 이력 조회
    List<ProductPriceHistoryDTO> getPriceHistoryByDateRange(java.time.LocalDateTime startDate, 
                                                           java.time.LocalDateTime endDate);
    
    // 제품 정보 조회
    ItemStockDTO getItemStock(Long itemStockId);
}
