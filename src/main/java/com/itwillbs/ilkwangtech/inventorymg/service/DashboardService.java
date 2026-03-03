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
import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.Nullable;
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
    private final InventoryHistoryRepository historyRepository; // 이력 레포지토리 추가
    private final ProductionPlaneRepository productionPlaneRepository;
    private final ProductionInsturctRepository productionInstructRepository;
    private final BomRepository bomRepository;

    // 창고 상태 확인 로직
    public Map<String, String> getWarehouseStatus() {
        Map<String, String> statusMap = new HashMap<>();
        String[] zones = {"ZONE A", "ZONE B", "ZONE C"};
        
        // 랙 개수를 25개로 자동 생성
        String[] racks = new String[25];
        for (int i = 0; i < 25; i++) {
            racks[i] = String.format("Rack %02d", i + 1);
        }

        for (String zone : zones) {
            for (String rack : racks) {
                Long qty = inventoryRepository.sumQuantityByZoneAndRack(zone, rack);
                
                // 수용치 10,000 미만은 보통(normal), 10,000 이상은 많음(full)
                String status = (qty == null || qty == 0) ? "none" : (qty < 10000 ? "normal" : "full");
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
                ItemEntity item = line.getItem();

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
    
    // 구매팀 제품 입고 시 품목 코드에 따라 타겟 창고를 지정하고 분산 저장하는 로직
    @Transactional
    public void receiveAndDistributeInventory(ItemEntity item, Long totalQuantity, String baseLotNumber) {
    	
    	String[] racks = new String[25];
        for (int i = 0; i < 25; i++) {
            racks[i] = String.format("Rack %02d", i + 1);
        }
        Random random = new Random();

        // 품목 코드(itemCode) 앞자리를 확인하여 타겟 창고(ZONE) 결정
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

        // 결정된 타겟 ZONE 안에서만 수량을 3등분하여 랜덤 Rack 3곳에 분산 저장
        long partQuantity = totalQuantity / 3;
        long remainder = totalQuantity % 3;

        for (int i = 0; i < 3; i++) {
            InventoryEntity inventory = new InventoryEntity();
            inventory.setItem(item);
            inventory.setLotNumber(baseLotNumber + "-" + (i + 1) + "-" + (System.currentTimeMillis() % 10000)); // 예: LOT-20260301-L123-1
            
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
    
    // DB에서 Object 배열로 가져온 결과를 자바 Map으로 예쁘게 변환하는 헬퍼 메서드
    private Map<Long, Long> convertToMap(List<Object[]> list) {
        Map<Long, Long> map = new HashMap<>();
        for (Object[] obj : list) {
        	if (obj[0] != null && obj[1] != null) {
                Long itemId;
                
                if (obj[0] instanceof ItemEntity) {
                    itemId = ((ItemEntity) obj[0]).getItemId(); // 객체면 ID만 꺼내오기!
                } else {
                    itemId = ((Number) obj[0]).longValue();     // 숫자면 그대로 쓰기!
                }
                
                map.put(itemId, ((Number) obj[1]).longValue());
            }
        }
        return map;
    }

    // (기존 메서드 교체) 초고속 MRP 기반 발주 필요 리스트 로직
    @Transactional(readOnly = true)
    public List<OrderNeededItemDTO> getOrderNeededList() {
        // 순수 아이템 82개 가져오기
        List<ItemEntity> uniqueItems = itemRepository.findAllDistinct();

        // 반복문 밖에서 단 7번의 쿼리로 전체 품목 데이터를 Map으로 조회
        Map<Long, Long> currentStockMap = convertToMap(inventoryRepository.sumCurrentQuantityGrouped());
        Map<Long, Long> incomingStockMap = convertToMap(purchaseOrderRepository.sumIncomingQuantityGrouped());
        Map<Long, Long> directPlanMap = convertToMap(productionPlaneRepository.sumProductionPlanQtyGrouped());
        Map<Long, Long> dependentPlanMap = convertToMap(bomRepository.sumDependentPlanQtyGrouped());
        Map<Long, Long> directReservedMap = convertToMap(productionInstructRepository.sumReservedQtyGrouped());
        Map<Long, Long> dependentReservedMap = convertToMap(bomRepository.sumDependentInstructQtyGrouped());
        Map<Long, Long> pendingReqMap = convertToMap(prLineRepository.sumQuantityGrouped());

        List<OrderNeededItemDTO> resultList = new ArrayList<>();
        long safeStockThreshold = 3000L; 

        // 이제 반복문 안에서는 DB를 절대 호출하지 않고, 메모리(Map)에서 값만 조회
        for (ItemEntity item : uniqueItems) {
        	
        	// 품목 코드가 SAM으로 시작하는 완제품은 발주 리스트 제외
            if (item.getItemCode() != null && item.getItemCode().toUpperCase().startsWith("SAM-")) {
                continue; 
            }
        	
            Long itemId = item.getItemId();

            // Map에서 값 꺼내기 (값이 없으면 기본값 0L 반환)
            long currentStock = currentStockMap.getOrDefault(itemId, 0L);
            long incomingStock = incomingStockMap.getOrDefault(itemId, 0L);
            long prodPlan = directPlanMap.getOrDefault(itemId, 0L) + dependentPlanMap.getOrDefault(itemId, 0L);
            long reservedStock = directReservedMap.getOrDefault(itemId, 0L) + dependentReservedMap.getOrDefault(itemId, 0L);
            long pendingReqQty = pendingReqMap.getOrDefault(itemId, 0L);
            
            String currentStatus = pendingReqQty > 0 ? "요청완료" : "발주대기";
            
            long availableStock = currentStock + incomingStock;
            long totalRequirement = safeStockThreshold + prodPlan + reservedStock; 
            
            long requiredQty = totalRequirement - availableStock; 
            if (requiredQty < 0) requiredQty = 0L; 

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

    // 4. 발주 요청 처리 (구매팀 헤더 및 라인 인서트)
    @Transactional
    public void createPurchaseRequests(List<Map<String, Long>> requestList) {
        if(requestList == null || requestList.isEmpty()) return;

        // 헤더 생성
        PurchaseRequestHeaderEntity header = new PurchaseRequestHeaderEntity();
        String prCode = "PR-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + (int)(Math.random()*1000);
        header.setPurchaseRequestCode(prCode);
        header.setRequestDate(LocalDate.now());
        PurchaseRequestHeaderEntity savedHeader = prHeaderRepository.save(header);

        // 라인 생성
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
    
    // 금일 입고 예정 갯수 가져오기
    public long getInboundScheduledCount() {
        // 실제 DB에서 출하완료/검수완료 상태인 발주 건수를 가져옵니다.
        return purchaseOrderRepository.countInboundScheduled();
    }
    
    // 금일 입고 처리 완료 건수
    public long getInboundProcessedToday() {
        return historyRepository.countProcessedToday(LocalDate.now(), "IN");
    }

    // 금일 출고 처리 완료 건수
    public long getOutboundProcessedToday() {
        return historyRepository.countProcessedToday(LocalDate.now(), "OUT");
    }
    
    // 랙 상세 정보 가져오기
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
    
    // 새로운 재고 이동 메서드 추가 (파일 맨 아래에 추가)
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
    
    // 차트 데이터 집계 (월/주/일)
    public ChartDataDTO getChartData(String type) {
        ChartDataDTO dto = new ChartDataDTO();
        // 날짜 순서를 보장하기 위해 LinkedHashMap 사용 (값 배열: [입고, 출고, 폐기])
        Map<String, long[]> dataMap = new LinkedHashMap<>(); 
        LocalDate now = LocalDate.now();
        LocalDate startDate;

        // 1. 선택된 기간(월/주/일)에 맞춰 X축 빈 껍데기(0 세팅) 먼저 생성
        if ("day".equals(type)) {
            startDate = now.minusDays(14); // 최근 15일
            for (int i = 14; i >= 0; i--) {
                dataMap.put(now.minusDays(i).format(DateTimeFormatter.ofPattern("MM-dd")), new long[]{0, 0, 0});
            }
        } else if ("week".equals(type)) {
            startDate = now.minusWeeks(10); // 최근 11주
            for (int i = 10; i >= 0; i--) {
                LocalDate weekDate = now.minusWeeks(i).with(java.time.DayOfWeek.MONDAY); // 해당 주의 월요일 기준
                dataMap.put(weekDate.format(DateTimeFormatter.ofPattern("MM-dd")) + "주", new long[]{0, 0, 0});
            }
        } else { // 기본값: month
            startDate = now.minusMonths(11); // 최근 12개월
            for (int i = 11; i >= 0; i--) {
                dataMap.put(now.minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")), new long[]{0, 0, 0});
            }
        }

        // 2. DB에서 기간 내 데이터 가져오기
        List<InventoryHistoryEntity> histories = historyRepository.findByTransactionDateAfter(startDate.minusDays(1));

        // 3. 가져온 데이터를 빈 껍데기에 매핑하여 누적합(SUM) 계산
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

        // 4. DTO에 결과 담기
        dto.setCategories(new ArrayList<>(dataMap.keySet()));
        dto.setInData(dataMap.values().stream().map(v -> v[0]).collect(Collectors.toList()));
        dto.setOutData(dataMap.values().stream().map(v -> v[1]).collect(Collectors.toList()));
        dto.setDiscardData(dataMap.values().stream().map(v -> v[2]).collect(Collectors.toList()));

        return dto;
    }
    
    // 금일 출고 대기 리스트 조회
    public List<OutboundItemDTO> getOutboundScheduledList() {
        List<OutboundItemDTO> list = new ArrayList<>();
        
        // 자재 출고 (작업지시 기반)
        List<Object[]> materials = productionInstructRepository.findMaterialOutboundList();
        for (Object[] obj : materials) {
            list.add(OutboundItemDTO.builder()
                    .type("자재출고")
                    .refCode((String) obj[0])
                    .itemCode((String) obj[1])
                    .itemName((String) obj[2])
                    .requiredQty(((Number) obj[3]).longValue())
                    .build());
        }
        
        // TODO: 완제품 출고(수주 기반) 로직은 영업팀 Order 테이블 구조에 맞춰 이 아래에 추가하시면 됩니다!
        return list;
    }

    // 출고 처리 실행 (재고 차감 및 이력 저장)
    @Transactional
    public String processOutbound() {
        List<Object[]> pendingMaterials = productionInstructRepository.findMaterialOutboundList();
        
        if (pendingMaterials.isEmpty()) {
            return "출고 대기 중인 항목이 없습니다.";
        }

        for (Object[] obj : pendingMaterials) {
            String instructCode = (String) obj[0];
            String itemCode = (String) obj[1];
            Long requiredQty = ((Number) obj[3]).longValue();

            // 출고할 자재의 현재 창고 재고들을 유통기한(또는 ID) 빠른 순서대로 가져옴 (FIFO: 선입선출)
            List<InventoryEntity> stocks = inventoryRepository.findByItemItemCodeOrderByExpirationDateAsc(itemCode);
            
            long remainingToDeduct = requiredQty;

            for (InventoryEntity stock : stocks) {
                if (remainingToDeduct <= 0) break; // 다 차감했으면 종료

                long currentQty = stock.getCurrentQuantity();
                if (currentQty == 0) continue;

                long deductQty = Math.min(currentQty, remainingToDeduct);
                stock.setCurrentQuantity(currentQty - deductQty); // 재고 차감
                remainingToDeduct -= deductQty;

                // 💡 출고(OUT) 이력 저장 (차트에 반영됨!)
                InventoryHistoryEntity history = new InventoryHistoryEntity();
                history.setItem(stock.getItem());
                history.setTransactionDate(LocalDate.now());
                history.setTransactionType("OUT");
                history.setQuantity(deductQty);
                historyRepository.save(history);
            }

            if (remainingToDeduct > 0) {
                throw new RuntimeException("재고가 부족하여 출고할 수 없습니다: " + itemCode + " (부족수량: " + remainingToDeduct + ")");
            }

            // 재고가 모두 정상 출고되었다면, 작업지시 상태를 'PROGRESS(진행중)'로 변경!
            productionInstructRepository.updateInstructStatus(instructCode, "PROGRESS");
        }
        
        return "출고 처리가 완료되었습니다.";
    }

}