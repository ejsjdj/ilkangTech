package com.itwillbs.ilkwangtech.inventorymg.service;

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

    // 출고 목록 조회 핵심 로직
    public List<OutboundListDTO> getOutboundListData(String tab, String searchType, String keyword) {
        List<OutboundListDTO> allData = new ArrayList<>();

        // 1. 생산팀 (Production) 출고 내역 수집
        String prodSql = 
            "SELECT MAX(p.item_type), p.item_name, SUM(target.req_qty), MAX(i.start_date) " +
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
                .lotNumber("-") // 복수의 원자재가 출고되므로 LOT는 하이픈 처리
                .itemName((String) obj[1])
                .outboundQty(((Number) obj[2]).longValue())
                .outboundDate(formatDate(obj[3]))
                .requestDept("생산팀")
                .build());
        }

        // 2. 영업팀 (Sales) 출고 내역 수집
        String salesSql = 
            "SELECT i.item_type, i.item_name, oi.quantity, so.expected_delivery_date " +
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

    // DB의 ItemType을 한글로 예쁘게 변환
    private String convertItemType(Object typeObj) {
        if (typeObj == null) return "원자재"; 
        String typeStr = typeObj.toString().toUpperCase();
        
        // 💡 유저님의 실제 DB 저장 순서에 맞게 조건 변경 (0: 완제품, 1: 원자재, 2: 반제품)
        if (typeStr.equals("0") || typeStr.contains("FINISHED")) return "완제품";
        if (typeStr.equals("1") || typeStr.contains("RAW")) return "원자재";
        if (typeStr.equals("2") || typeStr.contains("SEMI")) return "반제품";
        
        return "원자재"; // 매핑 실패 시 기본값
    }

    // 날짜(Datetime)를 화면 규격(YYYY-MM-DD)에 맞게 자르기
    private String formatDate(Object dateObj) {
        if (dateObj == null) return LocalDate.now().toString();
        String dateStr = dateObj.toString();
        return dateStr.length() >= 10 ? dateStr.substring(0, 10) : dateStr;
    }
}
