package com.itwillbs.ilkwangtech.inventorymg.service;

import com.itwillbs.ilkwangtech.inventorymg.dto.InventoryListDTO;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryHistoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryHistoryRepository;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 💡 반드시 추가!

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryListService {

    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public List<InventoryListDTO> getFilteredInventoryList(String tab, String searchType, String keyword) {
        
        // 전체 재고 조회 (수량이 0보다 큰 것만)
        List<InventoryEntity> allInventory = inventoryRepository.findAll().stream()
                .filter(i -> i.getCurrentQuantity() != null && i.getCurrentQuantity() > 0)
                .collect(Collectors.toList());

        return allInventory.stream()
                .filter(i -> filterByTab(i, tab))           // 탭 분류 필터링
                .filter(i -> filterBySearch(i, searchType, keyword)) // 검색 필터링
                .map(i -> {
                    // 방어 로직: DB에 Item이나 Code가 비어있어도 에러가 나지 않도록 처리
                    String code = (i.getItem() != null && i.getItem().getItemCode() != null) 
                                  ? i.getItem().getItemCode().toUpperCase() : "UNKNOWN";
                                  
                    String type = code.startsWith("RW") ? "원자재" : 
                                  (code.startsWith("SAM") ? "완제품" : "반제품");

                    String itemName = (i.getItem() != null && i.getItem().getItemName() != null) 
                                      ? i.getItem().getItemName() : "품목명 없음";

                    return InventoryListDTO.builder()
                            .id(i.getId())
                            .lotNumber(i.getLotNumber())
                            .itemName(itemName)
                            .currentQuantity(i.getCurrentQuantity())
                            .scheduledOutbound(0L) // TODO: 작업지시 할당 시 연동
                            .location(i.getZone() + " - " + i.getRack())
                            .inboundDate(LocalDate.now().toString()) // 임시 오늘 날짜
                            .itemType(type)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 탭 필터 로직
    private boolean filterByTab(InventoryEntity i, String tab) {
        if ("ALL".equals(tab)) return true;
        
        String code = (i.getItem() != null && i.getItem().getItemCode() != null) 
                      ? i.getItem().getItemCode().toUpperCase() : "";
                      
        if ("RAW".equals(tab) && code.startsWith("RW")) return true;
        if ("SEMI".equals(tab) && (code.startsWith("ST") || code.startsWith("IN"))) return true;
        if ("FINISHED".equals(tab) && code.startsWith("SAM")) return true;
        
        return false;
    }

    // 검색어 필터 로직
    private boolean filterBySearch(InventoryEntity i, String searchType, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return true; // 검색어 없으면 모두 통과
        
        String word = keyword.toLowerCase();
        return switch (searchType) {
            case "itemName" -> i.getItem() != null && i.getItem().getItemName() != null && i.getItem().getItemName().toLowerCase().contains(word);
            case "lotNumber" -> i.getLotNumber() != null && i.getLotNumber().toLowerCase().contains(word);
            case "location" -> i.getZone() != null && i.getRack() != null && (i.getZone() + i.getRack()).toLowerCase().contains(word);
            case "inboundDate" -> true; // 날짜 검색은 포맷 변환이 필요하므로 임시 통과
            default -> true;
        };
    }
    
    // 실수량 조절 (증가/감소) 로직
    @Transactional
    public String adjustInventory(Long inventoryId, String type, Long qty, String reason) {
        InventoryEntity inv = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("재고 정보를 찾을 수 없습니다."));

        if ("DECREASE".equals(type) && inv.getCurrentQuantity() < qty) {
            throw new RuntimeException("감소할 수량이 현재 재고보다 많습니다.");
        }

        // 수량 증감 처리
        if ("INCREASE".equals(type)) {
            inv.setCurrentQuantity(inv.getCurrentQuantity() + qty);
        } else {
            inv.setCurrentQuantity(inv.getCurrentQuantity() - qty);
        }

        // 이력 기록 (차트에 반영하기 위해 증가면 IN, 감소면 OUT으로 처리)
        InventoryHistoryEntity history = new InventoryHistoryEntity();
        history.setItem(inv.getItem());
        history.setTransactionDate(LocalDate.now());
        history.setTransactionType("INCREASE".equals(type) ? "IN" : "OUT");
        history.setQuantity(qty);
        historyRepository.save(history);

        return "수량 조절이 완료되었습니다. (사유: " + reason + ")";
    }
    
    // 폐기 로직
    @Transactional
    public String discardInventory(Long inventoryId, Long qty, String reason) {
        InventoryEntity inv = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("재고 정보를 찾을 수 없습니다."));

        if (inv.getCurrentQuantity() < qty) {
            throw new RuntimeException("폐기할 수량이 현재 재고보다 많습니다.");
        }

        inv.setCurrentQuantity(inv.getCurrentQuantity() - qty);

        InventoryHistoryEntity history = new InventoryHistoryEntity();
        history.setItem(inv.getItem());
        history.setTransactionDate(LocalDate.now());
        history.setTransactionType("DISCARD"); // 차트 주황색(폐기) 라인에 반영됨!
        history.setQuantity(qty);
        historyRepository.save(history);

        return "폐기 처리가 완료되었습니다. (사유: " + reason + ")";
    }
}