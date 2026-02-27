package com.itwillbs.ilkwangtech.inventorymg.service;

import com.itwillbs.ilkwangtech.inventorymg.dto.OrderNeededItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.dto.RackItemDTO;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryHistoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryHistoryRepository;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestHeaderEntity;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderRepository;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseRequestHeaderRepository;
import com.itwillbs.ilkwangtech.sales.repository.PurchaseRequestRepository;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
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
    
    // 금일 입고 처리 완료 건수
    public long getInboundProcessedToday() {
        return historyRepository.countProcessedToday(LocalDate.now(), "IN");
    }

    // 금일 출고 처리 완료 건수
    public long getOutboundProcessedToday() {
        return historyRepository.countProcessedToday(LocalDate.now(), "OUT");
    }
    
    // 금일 입고 예정 갯수 가져오기
    public long getInboundScheduledCount() {
        // 실제 DB에서 출하완료/검수완료 상태인 발주 건수를 가져옵니다.
        return purchaseOrderRepository.countInboundScheduled();
    }

    // 2. 창고 상태 (더미 + 쿼리 조합 예시)
    public Map<String, String> getWarehouseStatus() {
        Map<String, String> statusMap = new HashMap<>();
        String[] zones = {"ZONE A", "ZONE B", "ZONE C"};
        String[] racks = {"Rack 01", "Rack 02", "Rack 03"};

        for (String zone : zones) {
            for (String rack : racks) {
                // DB에서 실제 해당 구역/랙의 총 수량을 가져옵니다.
                Long qty = inventoryRepository.sumQuantityByZoneAndRack(zone, rack);
                
                // 수용량 기준은 프로젝트에 맞게 조정하세요! 
                // 예: 0개: 없음(하양), 1~50개: 보통(파랑), 51개 이상: 많음(빨강)
                String status = (qty == 0) ? "none" : (qty <= 50 ? "normal" : "full");
                statusMap.put(zone + "_" + rack, status);
            }
        }
        return statusMap;
    }
    
    // COMPLETE 상태인 발주건을 조회하여 창고에 분배 및 입고 처리 (로그 및 방어 로직 추가)
    @Transactional
    public void processCompleteOrders() {
        List<PurchaseOrderHeaderEntity> completeOrders = purchaseOrderRepository.findCompleteOrders();

        if (completeOrders.isEmpty()) {
            System.out.println("========== [입고 처리] COMPLETE 상태인 발주 데이터가 없습니다. ==========");
            return;
        }

        for (PurchaseOrderHeaderEntity order : completeOrders) {
            System.out.println("========== [발주서 확인] 발주서 ID: " + order.getId() + " / 상세 품목 개수: " + order.getLines().size() + " ==========");

            if (order.getLines().isEmpty()) {
                System.out.println("  -> [건너뜀] 이 발주서에 등록된 상세 품목(Line) 데이터가 아예 없습니다! (DB 확인 필요)");
            }

            int successCount = 0; // 실제 창고에 들어간 횟수 카운트

            for (PurchaseOrderEntity line : order.getLines()) {
                System.out.println("  -> [품목 확인] 라인 ID: " + line.getId() + " / 품목 ID: " + line.getItem() + " / 수량: " + line.getQuantity());

                if (line.getItem() == null) {
                    System.out.println("    -> [건너뜀] 품목 ID가 NULL 입니다.");
                    continue;
                }

                // 실제 Item 테이블에 존재하는지 확인
                ItemEntity item = itemRepository.findById(line.getItem()).orElse(null);

                if (item == null) {
                    System.out.println("    -> [건너뜀] Item 테이블에 ID가 " + line.getItem() + "인 품목이 존재하지 않습니다.");
                } else if (line.getQuantity() == null || line.getQuantity() <= 0) {
                    System.out.println("    -> [건너뜀] 입고 수량이 0이거나 NULL 입니다.");
                } else {
                    // 모든 조건 통과! 창고 입고 시작
                    String baseLotNumber = order.getPurchaseOrderCode() + "-L" + line.getId();
                    receiveAndDistributeInventory(item, line.getQuantity(), baseLotNumber);

                    InventoryHistoryEntity history = new InventoryHistoryEntity();
                    history.setItem(item);
                    history.setTransactionDate(LocalDate.now());
                    history.setTransactionType("IN");
                    history.setQuantity(line.getQuantity());
                    historyRepository.save(history);

                    System.out.println("    -> [입고 완료] 창고 분배 및 이력 저장 성공!");
                    successCount++;
                }
            }

            // 물건이 단 하나라도 정상적으로 창고에 들어갔을 때만 STORED로 변경
            if (successCount > 0) {
                order.setStatus("STORED");
                System.out.println("========== [상태 변경] 발주서 ID " + order.getId() + " 입고 완료(STORED) ==========");
            } else {
                System.out.println("========== [상태 유지] 정상 입고된 품목이 없어 COMPLETE 상태를 유지합니다. ==========");
            }
        }
    }
    
    // 구매팀 제품 입고 시 창고 ABC에 랜덤 고르게 분포하는 로직
    @Transactional
    public void receiveAndDistributeInventory(ItemEntity item, Long totalQuantity, String baseLotNumber) {
        String[] zones = {"ZONE A", "ZONE B", "ZONE C"};
        String[] racks = {"Rack 01", "Rack 02", "Rack 03"};
        Random random = new Random();

        // 수량을 3등분 하여 고르게 분포
        long partQuantity = totalQuantity / 3;
        long remainder = totalQuantity % 3;

        for (int i = 0; i < 3; i++) {
            InventoryEntity inventory = new InventoryEntity();
            inventory.setItem(item);
            inventory.setLotNumber(baseLotNumber + "-" + (i + 1)); // 예: LOT20260227-1, -2, -3
            
            // 구역은 A, B, C 한 번씩 무조건 배정 / Rack 번호는 해당 구역 내에서 랜덤
            inventory.setZone(zones[i]); 
            inventory.setRack(racks[random.nextInt(racks.length)]); 
            
            // 마지막 C구역에 나누고 남은 나머지 수량을 몰아줌
            long finalQuantity = partQuantity + (i == 2 ? remainder : 0);
            inventory.setCurrentQuantity(finalQuantity);
            inventory.setExpirationDate(LocalDate.now().plusYears(1)); // 유통기한 임의 1년 뒤
            
            inventoryRepository.save(inventory); // DB 저장
        }
    }

    // 3. 발주 필요 리스트 (생산계획 등은 더미데이터 활용)
    public List<OrderNeededItemDTO> getOrderNeededList() {
        List<ItemEntity> items = itemRepository.findAll();
        List<OrderNeededItemDTO> list = new ArrayList<>();
        
        for (ItemEntity item : items) {
            // 더미 로직: 재고가 일정 수량 이하일 때만 발주 리스트에 추가
            long currentStock = (long) (Math.random() * 5000);
            long safeStock = 3000L;
            
            if(currentStock < safeStock) {
                long prodPlan = 1000L;
                long required = safeStock - currentStock + prodPlan; // 필요재고 더미 공식
                
                list.add(OrderNeededItemDTO.builder()
                        .itemId(item.getItemId())
                        .itemName(item.getItemName())
                        .currentStock(currentStock)
                        .incomingStock((long)(Math.random() * 50))
                        .productionPlan(prodPlan)
                        .reservedStock(prodPlan) // 출고완료 대체 더미
                        .safeStock(safeStock)
                        .requiredStock(required)
                        .uom(item.getUom() != null ? item.getUom() : "EA")
                        .build());
            }
        }
        return list;
    }

    // 4. 발주 요청 처리 (구매팀 헤더 및 라인 인서트)
    @Transactional
    public void createPurchaseRequests(List<Long> itemIds) {
        if(itemIds == null || itemIds.isEmpty()) return;

        // 1. 헤더 생성
        PurchaseRequestHeaderEntity header = new PurchaseRequestHeaderEntity();
        String prCode = "PR-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + (int)(Math.random()*1000);
        header.setPurchaseRequestCode(prCode);
        header.setRequestDate(LocalDate.now());
        // 필요시 member_id 등 세팅
        PurchaseRequestHeaderEntity savedHeader = prHeaderRepository.save(header);

        // 2. 라인 생성
        for (Long itemId : itemIds) {
            PurchaseRequestEntity line = new PurchaseRequestEntity();
            line.setHeader(savedHeader);
            line.setItemId(itemId);
            line.setQuantity(100L); // 기본 요청 수량 (실무에선 입력받아야 함)
            prLineRepository.save(line);
        }
    }
    
    // 랙 상세 정보 가져오기
    public List<RackItemDTO> getRackDetails(String zone, String rack) {
        List<InventoryEntity> inventoryList = inventoryRepository.findByZoneAndRack(zone, rack);
        
        return inventoryList.stream().map(inv -> {
            // ItemEntity가 Null일 경우를 대비한 안전한 처리
            String code = (inv.getItem() != null) ? inv.getItem().getItemCode() : "품목없음";
            String name = (inv.getItem() != null) ? inv.getItem().getItemName() : "이름없음";
            
            return RackItemDTO.builder()
                .itemCode(code)
                .itemName(name)
                .lotNumber(inv.getLotNumber())
                .quantity(inv.getCurrentQuantity())
                .expirationDate(inv.getExpirationDate())
                .build();
        }).collect(Collectors.toList());
    }
    
}