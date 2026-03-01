package com.itwillbs.ilkwangtech.inventorymg.service;

import com.itwillbs.ilkwangtech.inventorymg.dto.InboundItemDTO;
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
        String[] racks = {"Rack 01", "Rack 02", "Rack 03", "Rack 04", "Rack 05", "Rack 06", "Rack 07", "Rack 08", "Rack 09"};

        for (String zone : zones) {
            for (String rack : racks) {
                // DB에서 실제 해당 구역/랙의 총 수량을 가져옵니다.
    			Long qty = inventoryRepository.sumQuantityByZoneAndRack(zone, rack);
                
                // 변경된 수용량 기준: 1 ~ 3999는 보통(normal), 4000 이상은 많음(full)
                String status = (qty == 0) ? "none" : (qty < 4000 ? "normal" : "full");
                statusMap.put(zone + "_" + rack, status);
            }
        }
        return statusMap;
    }
    
    // COMPLETE 상태인 발주건을 조회하여 창고에 분배 및 입고 처리 (로그 및 방어 로직 추가)
    @Transactional
    public void processCompleteOrders() {
        List<PurchaseOrderHeaderEntity> completeOrders = purchaseOrderRepository.findCompleteOrders();

        for (PurchaseOrderHeaderEntity order : completeOrders) {
            for (PurchaseOrderEntity line : order.getLines()) {
                
                // 외래키 설정 시: line.getItem()이 이미 ItemEntity 객체를 반환함
                ItemEntity item = line.getItem(); 

                if (item != null && line.getQuantity() != null && line.getQuantity() > 0) {
                    String baseLotNumber = order.getPurchaseOrderCode() + "-L" + line.getId();
                    
                    // 창고 분배 로직 실행
                    receiveAndDistributeInventory(item, line.getQuantity(), baseLotNumber);

                    // 입고 이력 저장
                    InventoryHistoryEntity history = new InventoryHistoryEntity();
                    history.setItem(item);
                    history.setTransactionDate(LocalDate.now());
                    history.setTransactionType("IN");
                    history.setQuantity(line.getQuantity());
                    historyRepository.save(history);
                }
            }
            order.setStatus("STORED"); 
        }
    }
    
    // 구매팀 제품 입고 시 품목 코드에 따라 타겟 창고를 지정하고 분산 저장하는 로직
    @Transactional
    public void receiveAndDistributeInventory(ItemEntity item, Long totalQuantity, String baseLotNumber) {
        String[] racks = {"Rack 01", "Rack 02", "Rack 03", "Rack 04", "Rack 05", "Rack 06", "Rack 07", "Rack 08", "Rack 09"};
        Random random = new Random();

        // 1. 품목 코드(itemCode) 앞자리를 확인하여 타겟 창고(ZONE) 결정
        String targetZone = "ZONE A"; // 예외 대비 기본값 (원자재)
        String itemCode = item.getItemCode();
        
        if (itemCode != null) {
            itemCode = itemCode.toUpperCase(); // 소문자로 들어올 경우를 대비해 대문자로 안전하게 변환
            if (itemCode.startsWith("RW-")) {
                targetZone = "ZONE A"; // 원자재
            } else if (itemCode.startsWith("ST-") || itemCode.startsWith("IN-")) {
                targetZone = "ZONE B"; // 반제품
            } else if (itemCode.startsWith("SAM-")) {
                targetZone = "ZONE C"; // 완제품
            }
        }

        // 2. 결정된 타겟 ZONE 안에서만 수량을 3등분하여 랜덤 Rack 3곳에 분산 저장
        long partQuantity = totalQuantity / 3;
        long remainder = totalQuantity % 3;

        for (int i = 0; i < 3; i++) {
            InventoryEntity inventory = new InventoryEntity();
            inventory.setItem(item);
            inventory.setLotNumber(baseLotNumber + "-" + (i + 1)); // 예: LOT-20260301-L123-1
            
            inventory.setZone(targetZone); // 조건문으로 찾은 타겟 창고(A, B, C 중 하나) 고정 배정
            inventory.setRack(racks[random.nextInt(racks.length)]); // 해당 창고 내에서 랜덤으로 랙 번호 배정
            
            long finalQuantity = partQuantity + (i == 2 ? remainder : 0); // 마지막 분할 시 나머지 수량 짬처리
            inventory.setCurrentQuantity(finalQuantity);
            inventory.setExpirationDate(LocalDate.now().plusYears(1)); // 유통기한 임의 1년 뒤
            
            // 수량이 0보다 클 때만 실제로 창고에 저장
            if(finalQuantity > 0) {
                inventoryRepository.save(inventory); 
            }
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
            String code = (inv.getItem() != null) ? inv.getItem().getItemCode() : "품목없음";
            String name = (inv.getItem() != null) ? inv.getItem().getItemName() : "이름없음";
            return RackItemDTO.builder()
                .inventoryId(inv.getId()) // 👈 이 줄 추가!
                .itemCode(code)
                .itemName(name)
                .lotNumber(inv.getLotNumber())
                .quantity(inv.getCurrentQuantity())
                .expirationDate(inv.getExpirationDate())
                .build();
        }).collect(Collectors.toList());
    }
    
    // 재고 이동 메서드
    @Transactional
    public void transferInventory(Long inventoryId, String targetZone, String targetRack, Long transferQty) {
        InventoryEntity source = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("해당 재고를 찾을 수 없습니다."));

        if (source.getCurrentQuantity() < transferQty) {
            throw new RuntimeException("이동하려는 수량이 현재 재고보다 많습니다.");
        }

        if (source.getCurrentQuantity().equals(transferQty)) {
            // 전량 이동: 구역과 랙 번호만 바꿈
            source.setZone(targetZone);
            source.setRack(targetRack);
        } else {
            // 부분 이동(분할): 기존 수량을 줄이고, 이동할 랙에 새 데이터를 생성
            source.setCurrentQuantity(source.getCurrentQuantity() - transferQty);
            
            InventoryEntity target = new InventoryEntity();
            target.setItem(source.getItem());
            // LOT 번호 중복 방지를 위해 꼬리표(-M) 붙임
            target.setLotNumber(source.getLotNumber() + "-M" + System.currentTimeMillis() % 1000); 
            target.setZone(targetZone);
            target.setRack(targetRack);
            target.setCurrentQuantity(transferQty);
            target.setExpirationDate(source.getExpirationDate());
            inventoryRepository.save(target);
        }
    }
    
    // 금일 입고 예정 상세 리스트(모달용) 조회
    public List<InboundItemDTO> getInboundScheduledList() {
        // COMPLETE 상태인 헤더와 라인들을 가져옴
        List<PurchaseOrderHeaderEntity> completeOrders = purchaseOrderRepository.findCompleteOrders();
        List<InboundItemDTO> list = new ArrayList<>();
        
        for (PurchaseOrderHeaderEntity order : completeOrders) {
            for (PurchaseOrderEntity line : order.getLines()) {
                if (line.getItem() != null) {
                    list.add(InboundItemDTO.builder()
                            .purchaseOrderCode(order.getPurchaseOrderCode())
                            .company(order.getCompany())
                            .itemCode(line.getItem().getItemCode())
                            .itemName(line.getItem().getItemName())
                            .quantity(line.getQuantity())
                            .build());
                }
            }
        }
        return list;
    }
    
}