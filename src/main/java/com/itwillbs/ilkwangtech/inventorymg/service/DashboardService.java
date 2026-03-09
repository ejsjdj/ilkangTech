package com.itwillbs.ilkwangtech.inventorymg.service;

import com.itwillbs.ilkwangtech.inventorymg.dto.ChartDataDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.InboundItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.OrderNeededItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.OutboundItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.RackItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryHistoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryHistoryRepository;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.production.repository.ProductionInsturctRepository;
import com.itwillbs.ilkwangtech.production.repository.ProductionPlaneRepository;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestHeaderEntity;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderRepository;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseRequestHeaderRepository;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseRequestRepository;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.repository.BomRepository;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final PurchaseRequestHeaderRepository prHeaderRepository;
    private final PurchaseRequestRepository prLineRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryHistoryRepository historyRepository; 
    private final ProductionPlaneRepository productionPlaneRepository;
    private final ProductionInsturctRepository productionInstructRepository;
    private final BomRepository bomRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    // 창고 상태 확인 로직
    public Map<String, String> getWarehouseStatus() {
        Map<String, String> statusMap = new HashMap<>();
        String[] zones = {"ZONE A", "ZONE B", "ZONE C"};
        
        String[] racks = new String[25];
        for (int i = 0; i < 25; i++) {
            racks[i] = String.format("Rack %02d", i + 1);
        }

        for (String zone : zones) {
            for (String rack : racks) {
                Long qty = inventoryRepository.sumQuantityByZoneAndRack(zone, rack);
                String status = "none";
                
                if (qty != null && qty > 0) {
                    if ("ZONE C".equals(zone)) {
                        // 완제품(ZONE C)은 기존 기준 유지: 1만 개부터 '많음(빨간색)'
                        status = (qty < 10000) ? "normal" : "full";
                    } else {
                        // 원자재(ZONE A), 반제품(ZONE B)은 5만 개부터 '많음(빨간색)'
                        status = (qty < 50000) ? "normal" : "full";
                    }
                }
                statusMap.put(zone + "_" + rack, status);
            }
        }
        return statusMap;
    }
    
    // COMPLETE 상태인 발주건을 조회하여 창고에 분배 및 입고 처리
    @Transactional
    public void processCompleteOrders() {
        List<PurchaseOrderHeaderEntity> completeOrders = purchaseOrderRepository.findCompleteOrders();

        if (completeOrders.isEmpty()) {
            return;
        }

        for (PurchaseOrderHeaderEntity order : completeOrders) {
            int successCount = 0; 

            for (PurchaseOrderEntity line : order.getLines()) {
                if (line.getItem() == null) continue;

                ItemEntity item = line.getItem();

                if (item != null && line.getQuantity() != null && line.getQuantity() > 0) {
                    receiveAndDistributeInventory(item, line.getQuantity());

                    InventoryHistoryEntity history = new InventoryHistoryEntity();
                    history.setItem(item);
                    history.setTransactionDate(LocalDate.now());
                    history.setTransactionType("IN");
                    history.setQuantity(line.getQuantity());
                    historyRepository.save(history);
                    
                    prLineRepository.deleteByItemId(item.getItemId());

                    successCount++;
                }
            }

            if (successCount > 0) {
                order.setStatus("STORED");
            } 
        }
    }
    
    @Transactional
    public void receiveAndDistributeInventory(ItemEntity item, Long totalQuantity) {
        
        String[] racks = new String[25];
        for (int i = 0; i < 25; i++) {
            racks[i] = String.format("Rack %02d", i + 1);
        }
        Random random = new Random();

        String targetZone = "ZONE A"; 
        String itemCode = item.getItemCode() != null ? item.getItemCode().toUpperCase() : "UNKNOWN";
        
        if (itemCode.startsWith("RW-")) targetZone = "ZONE A";
        else if (itemCode.startsWith("ST-") || itemCode.startsWith("IN-")) targetZone = "ZONE B";
        else if (itemCode.startsWith("SAM-")) targetZone = "ZONE C";

        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String lotPrefix = itemCode + "-" + dateStr + "-";
        
        long currentSeq = inventoryRepository.countByLotNumberStartingWith(lotPrefix);
        long partQuantity = totalQuantity / 3;
        long remainder = totalQuantity % 3;

        for (int i = 0; i < 3; i++) {
            long finalQuantity = partQuantity + (i == 2 ? remainder : 0); 
            
            if(finalQuantity > 0) {
                currentSeq++; 
                String finalLotNumber = lotPrefix + String.format("%04d", currentSeq);
                
                InventoryEntity inventory = new InventoryEntity();
                inventory.setItem(item);
                inventory.setLotNumber(finalLotNumber); 
                inventory.setZone(targetZone); 
                inventory.setRack(racks[random.nextInt(racks.length)]); 
                inventory.setCurrentQuantity(finalQuantity);
                inventory.setExpirationDate(LocalDate.now().plusYears(1)); 
                
                inventoryRepository.save(inventory); 
            }
        }
    }
    
    private Map<Long, Long> convertToMap(List<Object[]> list) {
        Map<Long, Long> map = new HashMap<>();
        if (list == null) return map;
        
        for (Object[] row : list) {
            if (row[0] != null && row[1] != null) {
                Long key = ((Number) row[0]).longValue();
                Long value = ((Number) row[1]).longValue();
                map.put(key, value);
            }
        }
        return map;
    }

    @Transactional(readOnly = true)
    public List<OrderNeededItemDTO> getOrderNeededList() {
        List<ItemEntity> uniqueItems = itemRepository.findAllDistinct();

        Map<Long, Long> currentStockMap = convertToMap(inventoryRepository.sumCurrentQuantityGrouped());
        Map<Long, Long> incomingStockMap = convertToMap(purchaseOrderRepository.sumIncomingQuantityGrouped());
        Map<Long, Long> directPlanMap = convertToMap(productionPlaneRepository.sumProductionPlanQtyGrouped());
        Map<Long, Long> dependentPlanMap = convertToMap(bomRepository.sumDependentPlanQtyGrouped());
        Map<Long, Long> directReservedMap = convertToMap(productionInstructRepository.sumReservedQtyGrouped());
        Map<Long, Long> dependentReservedMap = convertToMap(bomRepository.sumDependentInstructQtyGrouped());
        Map<Long, Long> pendingReqMap = convertToMap(prLineRepository.sumQuantityGrouped());

        List<OrderNeededItemDTO> resultList = new ArrayList<>();
        long safeStockThreshold = 3000L; 

        for (ItemEntity item : uniqueItems) {
        	if (item.getItemCode() == null || !item.getItemCode().toUpperCase().startsWith("RW-")) {
                continue; 
            }
        	
            Long itemId = item.getItemId();
            
            long currentStock = currentStockMap.getOrDefault(itemId, 0L);
            if (currentStock == 0L) {
                Long dbStock = inventoryRepository.sumCurrentQuantityByItemId(itemId);
                currentStock = (dbStock != null) ? dbStock : 0L;
            }
            long incomingStock = incomingStockMap.getOrDefault(itemId, 0L);
            long prodPlan = directPlanMap.getOrDefault(itemId, 0L) + dependentPlanMap.getOrDefault(itemId, 0L);
            long reservedStock = directReservedMap.getOrDefault(itemId, 0L) + dependentReservedMap.getOrDefault(itemId, 0L);
            long pendingReqQty = pendingReqMap.getOrDefault(itemId, 0L);
            
            String currentStatus = pendingReqQty > 0 ? "요청완료" : "발주대기";
            
            long availableStock = currentStock + incomingStock;
            long totalRequirement = safeStockThreshold + prodPlan + reservedStock; 
            long requiredQty = totalRequirement - availableStock; 
            
            if (requiredQty <= 0) {
                continue;
            }

            resultList.add(OrderNeededItemDTO.builder()
                    .itemId(itemId)
                    .itemName(item.getItemName())
                    .currentStock(currentStock)
                    .incomingStock(incomingStock)
                    .productionPlan(prodPlan)
                    .reservedStock(reservedStock)
                    .safeStock(safeStockThreshold)
                    .requiredStock(requiredQty)
                    .status(currentStatus)
                    .pendingRequestQty(pendingReqQty) 
                    .uom(item.getUom() != null ? item.getUom() : "EA")
                    .build());
        }
        
        resultList.sort((a, b) -> {
            int reqCompare = Long.compare(b.getRequiredStock(), a.getRequiredStock());
            return reqCompare != 0 ? reqCompare : a.getItemId().compareTo(b.getItemId());
        });
        
        return resultList;
    }

    @Transactional
    public void createPurchaseRequests(List<Map<String, Long>> requestList, String loginId) {
        if(requestList == null || requestList.isEmpty()) return;

        PurchaseRequestHeaderEntity header = new PurchaseRequestHeaderEntity();
        String prCode = "PR-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + (int)(Math.random()*1000);
        header.setPurchaseRequestCode(prCode);
        header.setRequestDate(LocalDate.now());

        Member member = memberRepository.findByEmployeeNumber(loginId) 
                .orElseThrow(() -> new RuntimeException("사원번호(" + loginId + ")에 해당하는 사용자 정보를 찾을 수 없습니다."));
                
        header.setMember(member); 

        PurchaseRequestHeaderEntity savedHeader = prHeaderRepository.save(header);

        for (Map<String, Long> data : requestList) {
            Long itemId = data.get("itemId");
            Long quantity = data.get("quantity");
            
            if (itemId != null && quantity != null && quantity > 0) {
                PurchaseRequestEntity line = new PurchaseRequestEntity();
                line.setHeader(savedHeader);
                ItemEntity findItem = itemRepository.findById(itemId).orElse(null);
                line.setItem(findItem); 
                line.setQuantity(quantity); 
                prLineRepository.save(line);
            }
        }
    }
    
    public long getInboundScheduledCount() {
        return purchaseOrderRepository.countInboundScheduled();
    }
    
    public long getInboundProcessedToday() {
        return historyRepository.countProcessedToday(LocalDate.now(), "IN");
    }

    public long getOutboundProcessedToday() {
        return historyRepository.countProcessedToday(LocalDate.now(), "OUT");
    }
    
    public List<RackItemDTO> getRackDetails(String zone, String rack) {
        List<InventoryEntity> inventoryList = inventoryRepository.findByZoneAndRack(zone, rack);
        return inventoryList.stream().map(inv -> {
            String code = (inv.getItem() != null) ? inv.getItem().getItemCode() : "품목없음";
            String name = (inv.getItem() != null) ? inv.getItem().getItemName() : "이름없음";
            return RackItemDTO.builder()
                .inventoryId(inv.getId())
                .itemCode(code)
                .itemName(name)
                .lotNumber(inv.getLotNumber())
                .quantity(inv.getCurrentQuantity())
                .expirationDate(inv.getExpirationDate())
                .build();
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public void transferInventory(Long inventoryId, String targetZone, String targetRack, Long transferQty) {
        InventoryEntity source = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("해당 재고를 찾을 수 없습니다."));

        if (source.getCurrentQuantity() < transferQty) {
            throw new RuntimeException("이동하려는 수량이 현재 재고보다 많습니다.");
        }

        if (source.getCurrentQuantity().equals(transferQty)) {
            source.setZone(targetZone);
            source.setRack(targetRack);
        } else {
            source.setCurrentQuantity(source.getCurrentQuantity() - transferQty);
            
            InventoryEntity target = new InventoryEntity();
            target.setItem(source.getItem());
            target.setLotNumber(source.getLotNumber() + "-M" + System.currentTimeMillis() % 1000); 
            target.setZone(targetZone);
            target.setRack(targetRack);
            target.setCurrentQuantity(transferQty);
            target.setExpirationDate(source.getExpirationDate());
            inventoryRepository.save(target);
        }
    }
    
    public List<InboundItemDTO> getInboundScheduledList() {
        List<PurchaseOrderHeaderEntity> completeOrders = purchaseOrderRepository.findCompleteOrders();
        List<InboundItemDTO> list = new ArrayList<>();
        
        for (PurchaseOrderHeaderEntity order : completeOrders) {
            for (PurchaseOrderEntity line : order.getLines()) {
                if (line.getItem() != null) {
                    list.add(InboundItemDTO.builder()
                            .purchaseOrderCode(order.getPurchaseOrderCode())
                            .company(order.getCompany() != null ? order.getCompany().getCompanyName() : "자체발주")
                            .itemCode(line.getItem().getItemCode())
                            .itemName(line.getItem().getItemName())
                            .quantity(line.getQuantity())
                            .build());
                }
            }
        }
        return list;
    }
    
    public ChartDataDTO getChartData(String type) {
        ChartDataDTO dto = new ChartDataDTO();
        Map<String, long[]> dataMap = new LinkedHashMap<>(); 
        LocalDate now = LocalDate.now();
        LocalDate startDate;

        if ("day".equals(type)) {
            startDate = now.minusDays(14); 
            for (int i = 14; i >= 0; i--) {
                dataMap.put(now.minusDays(i).format(DateTimeFormatter.ofPattern("MM-dd")), new long[]{0, 0, 0});
            }
        } else if ("week".equals(type)) {
            startDate = now.minusWeeks(10); 
            for (int i = 10; i >= 0; i--) {
                LocalDate weekDate = now.minusWeeks(i).with(java.time.DayOfWeek.MONDAY); 
                dataMap.put(weekDate.format(DateTimeFormatter.ofPattern("MM-dd")) + "주", new long[]{0, 0, 0});
            }
        } else { 
            startDate = now.minusMonths(11); 
            for (int i = 11; i >= 0; i--) {
                dataMap.put(now.minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")), new long[]{0, 0, 0});
            }
        }

        List<InventoryHistoryEntity> histories = historyRepository.findByTransactionDateAfter(startDate.minusDays(1));

        for (InventoryHistoryEntity h : histories) {
            String label = "";
            if ("day".equals(type)) {
                label = h.getTransactionDate().format(DateTimeFormatter.ofPattern("MM-dd"));
            } else if ("week".equals(type)) {
                LocalDate monday = h.getTransactionDate().with(java.time.DayOfWeek.MONDAY);
                label = monday.format(DateTimeFormatter.ofPattern("MM-dd")) + "주";
            } else {
                label = h.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            }

            if (dataMap.containsKey(label)) {
                long[] vals = dataMap.get(label);
                if ("IN".equals(h.getTransactionType())) vals[0] += h.getQuantity();
                else if ("OUT".equals(h.getTransactionType())) vals[1] += h.getQuantity();
                else if ("DISCARD".equals(h.getTransactionType())) vals[2] += h.getQuantity();
            }
        }

        dto.setCategories(new ArrayList<>(dataMap.keySet()));
        dto.setInData(dataMap.values().stream().map(v -> v[0]).collect(Collectors.toList()));
        dto.setOutData(dataMap.values().stream().map(v -> v[1]).collect(Collectors.toList()));
        dto.setDiscardData(dataMap.values().stream().map(v -> v[2]).collect(Collectors.toList()));

        return dto;
    }
    
    // 최근 출고 내역 통합 조회
    public List<OutboundItemDTO> getOutboundScheduledList() {
        List<OutboundItemDTO> list = new ArrayList<>();
        
        // 1. 자재 출고 실적 (생산팀)
        List<Object[]> materials = productionInstructRepository.findMaterialOutboundList();
        
        for (Object[] obj : materials) {
            String instructCode = (String) obj[0]; 
            Long itemId = ((Number) obj[1]).longValue();     
            String itemName = (String) obj[2];     
            Long requiredQty = ((Number) obj[3]).longValue(); 
            
            Long currentStock = inventoryRepository.getTotalQuantityByItemId(itemId);
            currentStock = (currentStock == null) ? 0L : currentStock;

            Long availableOutboundQty = Math.min(currentStock, requiredQty);
            Long shortageQty = Math.max(0L, requiredQty - currentStock);

            list.add(OutboundItemDTO.builder()
                    .type("생산 투입") 
                    .refCode(instructCode)
                    .itemCode(itemId) 
                    .itemName(itemName)
                    .requiredQty(requiredQty)
                    .currentStock(currentStock)
                    .availableOutboundQty(availableOutboundQty) 
                    .shortageQty(shortageQty)                  
                    .requestDept("생산팀")
                    .dueDate(LocalDate.now().toString()) 
                    .status(shortageQty > 0 ? "재고 부족" : "출고 완료") 
                    .build());
        }
        
        // 2. 완제품 출하 실적 (영업팀)
        String sql = "SELECT " +
                "  CONCAT('SO-', so.sales_order_id), " +  
                "  i.item_id, " +                 
                "  i.item_name, " +                 
                "  oi.quantity, " +                 
                "  so.expected_delivery_date " +    
                "FROM sales_order so " +
                "JOIN order_item oi ON so.sales_order_id = oi.sales_order_id " + 
                "JOIN item i ON oi.item_id = i.item_id ";

        @SuppressWarnings("unchecked")
        List<Object[]> salesList = entityManager.createNativeQuery(sql).getResultList();

        for (Object[] obj : salesList) {
            String orderCode = (String) obj[0];
            Long itemId = ((Number) obj[1]).longValue();
            String itemName = (String) obj[2];
            Long requiredQty = ((Number) obj[3]).longValue();
            String dueDate = (obj[4] != null) ? obj[4].toString().substring(0, 10) : "";

            Long currentStock = inventoryRepository.getTotalQuantityByItemId(itemId);
            currentStock = (currentStock == null) ? 0L : currentStock;

            Long availableOutboundQty = Math.min(currentStock, requiredQty);
            Long shortageQty = Math.max(0L, requiredQty - currentStock);

            list.add(OutboundItemDTO.builder()
                    .type("영업 출하") 
                    .refCode(orderCode)
                    .itemCode(itemId) 
                    .itemName(itemName)
                    .requiredQty(requiredQty)
                    .currentStock(currentStock)
                    .availableOutboundQty(availableOutboundQty)
                    .shortageQty(shortageQty)
                    .requestDept("영업팀")
                    .dueDate(dueDate)
                    .status(shortageQty > 0 ? "재고 부족" : "출고 완료")
                    .build());
        }

        return list;
    }
    

}