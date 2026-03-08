package com.itwillbs.ilkwangtech.inventorymg.service;

import com.itwillbs.ilkwangtech.common.annotation.Audit;
import com.itwillbs.ilkwangtech.inventorymg.dto.InventoryHistoryDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.InventoryListDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.OutboundListDTO;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryHistoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryHistoryRepository;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryListService {

    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository historyRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<InventoryListDTO> getFilteredInventoryList(String tab, String searchType, String keyword) {
        
        List<InventoryEntity> allInventory = inventoryRepository.findAll().stream()
                .filter(i -> i.getCurrentQuantity() != null)
                .collect(Collectors.toList());

        return allInventory.stream()
                .map(i -> {
                    try {
                        
                        if (i.getItem() == null) return null;
                        
                        String code = i.getItem().getItemCode() != null ? i.getItem().getItemCode().toUpperCase() : "UNKNOWN";
                        
                        String type = code.startsWith("RW") ? "원자재" : 
                                      (code.startsWith("SAM") ? "완제품" : "반제품");

                        String itemName = i.getItem().getItemName() != null ? i.getItem().getItemName() : "품목명 없음";

                        return InventoryListDTO.builder()
                                .id(i.getId())
                                .lotNumber(i.getLotNumber())
                                .itemName(itemName)
                                .currentQuantity(i.getCurrentQuantity())
                                .scheduledOutbound(0L) 
                                .location(i.getZone() + " - " + i.getRack())
                                .inboundDate(LocalDate.now().toString()) 
                                .itemType(type)
                                .build();
                                
                    } catch (jakarta.persistence.EntityNotFoundException e) {
                        return null; 
                    }
                })
                .filter(dto -> dto != null) // null로 반환된 불량 데이터 걸러내기
                .filter(dto -> filterByTabDTO(dto, tab))
                .filter(dto -> filterBySearchDTO(dto, searchType, keyword))
                .collect(Collectors.toList());
    }

    // 탭 필터 로직
    private boolean filterByTabDTO(InventoryListDTO dto, String tab) {
        if ("ALL".equals(tab)) return true;
        if ("RAW".equals(tab) && "원자재".equals(dto.getItemType())) return true;
        if ("FINISHED".equals(tab) && "완제품".equals(dto.getItemType())) return true;
        if ("SEMI".equals(tab) && "반제품".equals(dto.getItemType())) return true;
        return false;
    }

    // 검색어 필터 로직
    private boolean filterBySearchDTO(InventoryListDTO dto, String searchType, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return true; 
        
        String word = keyword.toLowerCase();
        return switch (searchType) {
            case "itemName" -> dto.getItemName() != null && dto.getItemName().toLowerCase().contains(word);
            case "lotNumber" -> dto.getLotNumber() != null && dto.getLotNumber().toLowerCase().contains(word);
            case "location" -> dto.getLocation() != null && dto.getLocation().toLowerCase().contains(word);
            case "inboundDate" -> true; 
            default -> true;
        };
    }
    
    // 실수량 조절 (증가/감소) 로직
    @Transactional
    @Audit(action = "재고 조정", entity = "Inventory")
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
    @Audit(action = "재고 폐기", entity = "Inventory")
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
        history.setTransactionType("DISCARD"); 
        history.setQuantity(qty);
        historyRepository.save(history);

        return "폐기 처리가 완료되었습니다. (사유: " + reason + ")";
    }

    // 출고 목록 조회 핵심 로직
    public List<OutboundListDTO> getOutboundListData(String tab, String searchType, String keyword) {
        List<OutboundListDTO> allData = new ArrayList<>();

        // 1. 생산팀 (Production) 출고 내역 수집
        String prodSql = 
                "SELECT MAX(p.item_code), p.item_name, SUM(target.req_qty), MAX(i.start_date) " +
                "FROM (" +
                "    SELECT i.id AS instruct_id, p.item_id, (COALESCE(i.instruct_qty, 0) * b.require_qty) AS req_qty " +
                "    FROM production_instruct i " +
                "    JOIN bom b ON i.item_id = b.child_item_id " +
                "    JOIN item p ON b.parent_item_id = p.item_id " +
                "    WHERE UPPER(i.status) IN ('PROGRESS', 'COMPLETE') " +
                "    UNION ALL " +
                "    SELECT i.id AS instruct_id, p.item_id, (w.addition_qty * b.require_qty) AS req_qty " +
                "    FROM production_instruct i " +
                "    JOIN production_worker w ON i.id = w.instruct_id " +
                "    JOIN bom b ON w.item_id = b.child_item_id " +
                "    JOIN item p ON b.parent_item_id = p.item_id " +
                "    WHERE UPPER(i.status) IN ('PROGRESS', 'COMPLETE') " +
                "      AND w.addition_qty IS NOT NULL AND w.addition_qty > 0 " +
                ") target " +
                "JOIN item p ON target.item_id = p.item_id " +
                "JOIN production_instruct i ON target.instruct_id = i.id " +
                "GROUP BY p.item_id, p.item_name, target.instruct_id " +
                "HAVING COALESCE(SUM(target.req_qty), 0) > 0";

        @SuppressWarnings("unchecked")
        List<Object[]> prodResults = entityManager.createNativeQuery(prodSql).getResultList();
        
        for (Object[] obj : prodResults) {
            allData.add(OutboundListDTO.builder()
                .itemType(convertItemType(obj[0]))
                .lotNumber("-") 
                .itemName((String) obj[1])
                .outboundQty(((Number) obj[2]).longValue())
                .outboundDate(formatDate(obj[3]))
                .requestDept("생산팀")
                .build());
        }

        // 2. 영업팀 (Sales) 출고 내역 수집
        String salesSql = 
                "SELECT i.item_code, i.item_name, oi.quantity, so.expected_delivery_date " +
                "FROM sales_order so " +
                "JOIN order_item oi ON so.sales_order_id = oi.sales_order_id " +
                "JOIN item i ON oi.item_id = i.item_id ";

        @SuppressWarnings("unchecked")
        List<Object[]> salesResults = entityManager.createNativeQuery(salesSql).getResultList();
        
        for (Object[] obj : salesResults) {
            allData.add(OutboundListDTO.builder()
                .itemType(convertItemType(obj[0]))
                .lotNumber("-") 
                .itemName((String) obj[1])
                .outboundQty(((Number) obj[2]).longValue())
                .outboundDate(formatDate(obj[3]))
                .requestDept("영업팀")
                .build());
        }

        // 3. 자바 Stream을 이용한 탭 & 검색어 필터링
        return allData.stream()
            // 탭 필터링
            .filter(dto -> {
                if ("ALL".equalsIgnoreCase(tab)) return true;
                if ("RAW".equalsIgnoreCase(tab)) return "원자재".equals(dto.getItemType());
                if ("SEMI".equalsIgnoreCase(tab)) return "반제품".equals(dto.getItemType());
                if ("FINISHED".equalsIgnoreCase(tab)) return "완제품".equals(dto.getItemType());
                if ("SALES".equalsIgnoreCase(tab)) return "영업팀".equals(dto.getRequestDept());
                if ("PRODUCTION".equalsIgnoreCase(tab)) return "생산팀".equals(dto.getRequestDept());
                return true;
            })
            // 검색어 필터링
            .filter(dto -> {
                if (keyword == null || keyword.trim().isEmpty()) return true;
                String k = keyword.toLowerCase();
                switch (searchType) {
                    case "itemName": return dto.getItemName() != null && dto.getItemName().toLowerCase().contains(k);
                    case "requestDept": return dto.getRequestDept() != null && dto.getRequestDept().toLowerCase().contains(k);
                    case "outboundDate": return dto.getOutboundDate() != null && dto.getOutboundDate().contains(k);
                    default: return true;
                }
            })
            .collect(Collectors.toList());
    }

    private String convertItemType(Object codeObj) {
        if (codeObj == null) return "UNKNOWN"; 
        String code = codeObj.toString().toUpperCase();
        
        if (code.startsWith("RW")) return "원자재";
        if (code.startsWith("SAM")) return "완제품";
        
        return "반제품"; 
    }

    // 날짜(Datetime)를 화면 규격(YYYY-MM-DD)에 맞게 자르기
    private String formatDate(Object dateObj) {
        if (dateObj == null) return LocalDate.now().toString();
        String dateStr = dateObj.toString();
        return dateStr.length() >= 10 ? dateStr.substring(0, 10) : dateStr;
    }
    
 // 💡 재고 이력(History) 데이터 조회 핵심 로직
    @Transactional(readOnly = true)
    public List<InventoryHistoryDTO> getInventoryHistoryData(String tab, String searchType, String keyword) {
        
        List<InventoryHistoryEntity> historyList = historyRepository.findAll();

        return historyList.stream()
            .filter(h -> h.getItem() != null) // 삭제된 품목 방어
            .map(h -> {
                String code = h.getItem().getItemCode() != null ? h.getItem().getItemCode().toUpperCase() : "UNKNOWN";
                String itemType = code.startsWith("RW") ? "원자재" : (code.startsWith("SAM") ? "완제품" : "반제품");
                
                // DB의 영문 상태값을 한글로 예쁘게 변환
                String txType = h.getTransactionType() != null ? h.getTransactionType().toUpperCase() : "";
                String korTxType = switch (txType) {
                    case "IN" -> "입고 (증가)";
                    case "OUT" -> "출고 (감소)";
                    case "DISCARD" -> "폐기";
                    default -> "기타 이동";
                };

                return InventoryHistoryDTO.builder()
                        .transactionDate(h.getTransactionDate().toString())
                        .transactionType(korTxType)
                        .itemType(itemType)
                        .itemName(h.getItem().getItemName())
                        .quantity(h.getQuantity())
                        .build();
            })
            // 탭 필터링 (전체/입고/출고/폐기)
            .filter(dto -> {
                if ("ALL".equals(tab)) return true;
                if ("IN".equals(tab) && dto.getTransactionType().contains("입고")) return true;
                if ("OUT".equals(tab) && dto.getTransactionType().contains("출고")) return true;
                if ("DISCARD".equals(tab) && dto.getTransactionType().contains("폐기")) return true;
                return false;
            })
            // 검색어 필터링 (품목명, 변동일, 변동유형, 품목구분 조건 추가)
            .filter(dto -> {
                if (keyword == null || keyword.trim().isEmpty()) return true;
                
                String k = keyword.toLowerCase();
                switch (searchType) {
                    case "itemName": 
                        return dto.getItemName() != null && dto.getItemName().toLowerCase().contains(k);
                    case "transactionDate": 
                        return dto.getTransactionDate() != null && dto.getTransactionDate().contains(k);
                    case "transactionType": 
                        return dto.getTransactionType() != null && dto.getTransactionType().toLowerCase().contains(k);
                    case "itemType": 
                        return dto.getItemType() != null && dto.getItemType().toLowerCase().contains(k);
                    default: 
                        return true;
                }
            })
            // 최신순 정렬
            .sorted((a, b) -> b.getTransactionDate().compareTo(a.getTransactionDate()))
            .collect(Collectors.toList());
    }
}
