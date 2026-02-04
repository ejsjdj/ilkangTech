package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.ItemStockDTO;
import com.itwillbs.ilkwangtech.sales.dto.PriceNegotiationDTO;
import com.itwillbs.ilkwangtech.sales.dto.ProductPriceHistoryDTO;
import com.itwillbs.ilkwangtech.sales.entity.*;
import com.itwillbs.ilkwangtech.sales.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceManagementServiceImpl implements PriceManagementService {

    private final ItemStockRepository itemStockRepository;
    private final CustomerRepository customerRepository;
    private final ProductPriceHistoryRepository priceHistoryRepository;
    private final WareHouseRepository warehouseRepository;

    @Override
    @Transactional
    public ItemStockDTO createItemStockWithPrice(ItemStockDTO itemStockDTO) {
        // 제품 생성
        ItemStock itemStock = new ItemStock();
        itemStock.setProductName(itemStockDTO.getProductName());
        itemStock.setProductCode(itemStockDTO.getProductCode());
        itemStock.setQuantity(itemStockDTO.getQuantity());
        itemStock.setUnit(itemStockDTO.getUnit());
        itemStock.setDescription(itemStockDTO.getDescription());
        itemStock.setCurrentUnitPrice(itemStockDTO.getCurrentUnitPrice());

        // 창고 설정
        if (itemStockDTO.getWarehouseId() != null) {
            WareHouse warehouse = warehouseRepository.findByWarehouseId(itemStockDTO.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + itemStockDTO.getWarehouseId()));
            itemStock.setWarehouse(warehouse);
        }

        ItemStock savedItemStock = itemStockRepository.save(itemStock);

        // 초기 단가 이력 기록
        if (itemStockDTO.getCurrentUnitPrice() != null && itemStockDTO.getCurrentUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            ProductPriceHistory priceHistory = new ProductPriceHistory(
                    savedItemStock, null, itemStockDTO.getCurrentUnitPrice(), 
                    null, "신규 제품 등록", "시스템"
            );
            priceHistoryRepository.save(priceHistory);
        }

        return convertToItemStockDTO(savedItemStock);
    }

    @Override
    @Transactional
    public ItemStockDTO updateProductPrice(Long itemStockId, BigDecimal newPrice, String changeReason, String registeredBy) {
        ItemStock itemStock = itemStockRepository.findByItemStockId(itemStockId)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + itemStockId));

        BigDecimal previousPrice = itemStock.getCurrentUnitPrice();

        // 이전 단가 이력 종료 처리
        priceHistoryRepository.endPreviousPrices(itemStockId, null, LocalDateTime.now());

        // 새로운 단가 설정
        itemStock.setCurrentUnitPrice(newPrice);
        ItemStock updatedItemStock = itemStockRepository.save(itemStock);

        // 단가 변경 이력 기록
        ProductPriceHistory priceHistory = new ProductPriceHistory(
                updatedItemStock, null, newPrice, previousPrice, changeReason, registeredBy
        );
        priceHistoryRepository.save(priceHistory);

        return convertToItemStockDTO(updatedItemStock);
    }

    @Override
    @Transactional
    public ProductPriceHistoryDTO negotiateCustomerPrice(PriceNegotiationDTO negotiationDTO) {
        // 제품 확인
        ItemStock itemStock = itemStockRepository.findByItemStockId(negotiationDTO.getItemStockId())
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + negotiationDTO.getItemStockId()));

        // 고객사 확인
        Customer customer = customerRepository.findByCustomerId(negotiationDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + negotiationDTO.getCustomerId()));

        // 현재 고객사별 단가 조회
        Optional<ProductPriceHistory> currentPriceOpt = priceHistoryRepository.findCurrentPrice(
                negotiationDTO.getItemStockId(), negotiationDTO.getCustomerId(), LocalDateTime.now());

        BigDecimal previousPrice = currentPriceOpt.map(ProductPriceHistory::getUnitPrice).orElse(null);

        // 이전 고객사별 단가 이력 종료 처리
        priceHistoryRepository.endPreviousPrices(negotiationDTO.getItemStockId(), negotiationDTO.getCustomerId(), LocalDateTime.now());

        // 새로운 협상 단가 기록
        ProductPriceHistory newPriceHistory = new ProductPriceHistory(
                itemStock, customer, negotiationDTO.getNewPrice(), previousPrice,
                negotiationDTO.getChangeReason(), negotiationDTO.getSalesPerson()
        );
        ProductPriceHistory savedHistory = priceHistoryRepository.save(newPriceHistory);

        return convertToPriceHistoryDTO(savedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCurrentPrice(Long itemStockId, Long customerId) {
        // 고객사별 단가 우선 조회
        if (customerId != null) {
            Optional<ProductPriceHistory> customerPrice = priceHistoryRepository.findCurrentPrice(
                    itemStockId, customerId, LocalDateTime.now());
            if (customerPrice.isPresent()) {
                return customerPrice.get().getUnitPrice();
            }
        }

        // 기본 단가 조회
        Optional<ProductPriceHistory> basePrice = priceHistoryRepository.findCurrentPrice(
                itemStockId, null, LocalDateTime.now());
        
        if (basePrice.isPresent()) {
            return basePrice.get().getUnitPrice();
        }

        // ItemStock의 현재 단가 fallback
        Optional<ItemStock> itemStock = itemStockRepository.findByItemStockId(itemStockId);
        return itemStock.map(ItemStock::getCurrentUnitPrice).orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceHistoryDTO> getPriceHistory(Long itemStockId) {
        List<ProductPriceHistory> histories = priceHistoryRepository.findByItemStock_ItemStockIdOrderByEffectiveDateDesc(itemStockId);
        return histories.stream()
                .map(this::convertToPriceHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceHistoryDTO> getCustomerPriceHistory(Long customerId, Long itemStockId) {
        List<ProductPriceHistory> histories = priceHistoryRepository.findByCustomer_CustomerIdAndItemStock_ItemStockIdOrderByEffectiveDateDesc(
                customerId, itemStockId);
        return histories.stream()
                .map(this::convertToPriceHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceHistoryDTO> getAllCurrentPrices() {
        List<ProductPriceHistory> currentPrices = priceHistoryRepository.findAllCurrentPrices(LocalDateTime.now());
        return currentPrices.stream()
                .map(this::convertToPriceHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceHistoryDTO> getPriceHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<ProductPriceHistory> histories = priceHistoryRepository.findByDateRange(startDate, endDate);
        return histories.stream()
                .map(this::convertToPriceHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemStockDTO getItemStock(Long itemStockId) {
        ItemStock itemStock = itemStockRepository.findByItemStockId(itemStockId)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + itemStockId));
        return convertToItemStockDTO(itemStock);
    }

    // DTO 변환 메서드
    private ItemStockDTO convertToItemStockDTO(ItemStock itemStock) {
        ItemStockDTO dto = new ItemStockDTO();
        dto.setItemStockId(itemStock.getItemStockId());
        dto.setWarehouseId(itemStock.getWarehouse() != null ? itemStock.getWarehouse().getWarehouseId() : null);
        dto.setWarehouseName(itemStock.getWarehouse() != null ? itemStock.getWarehouse().getWarehouseName() : null);
        dto.setProductName(itemStock.getProductName());
        dto.setProductCode(itemStock.getProductCode());
        dto.setQuantity(itemStock.getQuantity());
        dto.setUnit(itemStock.getUnit());
        dto.setDescription(itemStock.getDescription());
        dto.setCurrentUnitPrice(itemStock.getCurrentUnitPrice());
        return dto;
    }

    private ProductPriceHistoryDTO convertToPriceHistoryDTO(ProductPriceHistory history) {
        ProductPriceHistoryDTO dto = new ProductPriceHistoryDTO();
        dto.setPriceHistoryId(history.getPriceHistoryId());
        dto.setItemStockId(history.getItemStock().getItemStockId());
        dto.setProductName(history.getItemStock().getProductName());
        dto.setCustomerId(history.getCustomer() != null ? history.getCustomer().getCustomerId() : null);
        dto.setCustomerName(history.getCustomer() != null ? history.getCustomer().getCustomerName() : null);
        dto.setUnitPrice(history.getUnitPrice());
        dto.setPreviousPrice(history.getPreviousPrice());
        dto.setEffectiveDate(history.getEffectiveDate());
        dto.setEndDate(history.getEndDate());
        dto.setChangeReason(history.getChangeReason());
        dto.setRegisteredBy(history.getRegisteredBy());
        dto.setCreatedAt(history.getCreatedAt());
        return dto;
    }
}
