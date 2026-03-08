package com.itwillbs.ilkwangtech.sales.service.order;

import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository;
import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initTable() {
        log.info("Checking SALES_ORDER and ORDER_ITEM table schema...");
        try {
            // 1. sales_order 테이블의 order_status 타입 변경 검사
            checkAndFixOrderStatusColumn();
            
            // 2. order_item 테이블에 누락된 컬럼 추가
            checkAndAddColumn("ORDER_ITEM", "DELIVERY_DATE", "DATE");
            checkAndAddColumn("ORDER_ITEM", "DETAIL_STATUS", "NUMBER(10) DEFAULT 10");
        } catch (Exception e) {
            log.error("Failed to update SALES_ORDER or ORDER_ITEM table schema: {}", e.getMessage());
        }
    }

    private void checkAndFixOrderStatusColumn() {
        // Oracle의 USER_TAB_COLUMNS에서 데이터 타입을 확인
        String sql = "SELECT data_type FROM user_tab_columns WHERE table_name = 'SALES_ORDER' AND column_name = 'ORDER_STATUS'";
        try {
            String dataType = jdbcTemplate.queryForObject(sql, String.class);
            if (dataType != null && (dataType.contains("CHAR") || dataType.contains("VARCHAR"))) {
                log.info("Converting SALES_ORDER.ORDER_STATUS from {} to NUMBER...", dataType);
                
                // 1. 기존 데이터 백업 또는 임시 컬럼 생성 (안전한 전환을 위해)
                // 여기서는 간단하게 기존 데이터를 지우거나, 호환되는 경우 바로 변경 시도
                // 데이터가 있을 경우 바로 MODIFY가 안될 수 있으므로 주의가 필요하지만, 
                // 개발 단계에서는 컬럼을 드롭하고 새로 만드는 것이 확실할 수 있음.
                // 하지만 운영 데이터를 고려하여 이름을 바꾸고 새로 만들고 데이터를 옮기는 방식이 권장됨.
                
                // 만약 'PENDING' 같은 문자열이 들어있다면 숫자로 바꿀 수 없으므로 데이터를 정리해야 함.
                jdbcTemplate.execute("UPDATE SALES_ORDER SET ORDER_STATUS = '10' WHERE ORDER_STATUS = 'PENDING'");
                jdbcTemplate.execute("UPDATE SALES_ORDER SET ORDER_STATUS = '40' WHERE ORDER_STATUS = 'COMPLETED'");
                
                // 컬럼 타입 변경 (데이터가 모두 숫자 형태 문자열이면 자동 변환될 수도 있음)
                // 하지만 Oracle은 컬럼에 데이터가 있으면 타입을 함부로 못바꿈.
                // 편법: 컬럼 이름을 바꾸고 새로 만든 뒤 데이터 복사
                jdbcTemplate.execute("ALTER TABLE SALES_ORDER RENAME COLUMN ORDER_STATUS TO ORDER_STATUS_OLD");
                jdbcTemplate.execute("ALTER TABLE SALES_ORDER ADD (ORDER_STATUS NUMBER(10) DEFAULT 10)");
                jdbcTemplate.execute("UPDATE SALES_ORDER SET ORDER_STATUS = TO_NUMBER(REGEXP_REPLACE(ORDER_STATUS_OLD, '[^0-9]', '10'))");
                jdbcTemplate.execute("ALTER TABLE SALES_ORDER DROP COLUMN ORDER_STATUS_OLD");
                
                log.info("SALES_ORDER.ORDER_STATUS converted to NUMBER successfully.");
            }
        } catch (Exception e) {
            log.warn("Could not check or fix ORDER_STATUS column: {}", e.getMessage());
        }
    }

    private void checkAndAddColumn(String tableName, String columnName, String columnDef) {
        String checkSql = "SELECT count(*) FROM user_tab_columns WHERE table_name = ? AND column_name = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, tableName.toUpperCase(), columnName.toUpperCase());

        if (count == null || count == 0) {
            log.info("Adding column {} to table {}...", columnName, tableName);
            String alterSql = String.format("ALTER TABLE %s ADD (%s %s)", tableName, columnName, columnDef);
            jdbcTemplate.execute(alterSql);
            log.info("Column {} added successfully.", columnName);
        }
    }

    @Override
    @Transactional
    public void createOrder(OrderDTO orderDTO) {
        if (orderDTO.getOrderStatus() == null) {
            orderDTO.setOrderStatus(OrderStatus.PENDING);
        }
        orderRepository.saveOrderDTO(orderDTO);

        Long generatedOrderId = orderDTO.getOrderId();
        List<OrderDetailDTO> orderDetails = orderDTO.getOrderDetails();

        if (orderDetails != null && !orderDetails.isEmpty()) {
            for (OrderDetailDTO detail : orderDetails) {
                detail.setOrderId(generatedOrderId);
            }
            orderRepository.saveOrderDetail(orderDetails);
        }

        // 수주 등록 후 즉시 재고 기반 상태 업데이트 실행
        updateOrderStatusesBasedOnInventory();
    }


    @Override
    public List<OrderDTO> getOrderList(Pageable pageable) {
        return orderRepository.getOrderList(
                pageable.getOffset(),
                pageable.getPageSize()
        );
    }

    @Override
    public OrderDTO getOrder(Long id) {
        OrderDTO orderDTO = orderRepository.getOrder(id).orElseThrow();
        orderDTO.setOrderDetails(orderRepository.getOrderDetails(id));
        return orderDTO;
    }

    @Override
    public void update(OrderDTO orderDTO) {
        orderRepository.updateOrder(orderDTO);
    }

    @Override
    public boolean isEditable(Long id) {
        OrderDTO order = orderRepository.getOrder(id).orElseThrow();
        if (order == null) {
            return false;
        }

        return order.getOrderStatus() == OrderStatus.PENDING;
    }

    @Override
    public void invalid(Long id) {
        OrderDTO orderDTO = orderRepository.getOrder(id).orElseThrow();
        orderDTO.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.updateOrder(orderDTO);
    }

    @Transactional
    public void changeOrderStatus(Long id, OrderStatus newStatus) {
        // 1. 기존 주문 조회
        OrderDTO orderDTO = orderRepository.getOrder(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 수주 건이 존재하지 않습니다."));

        // 2. 상태 변경 로직 검증 (예: 배송 완료된 건은 상태 변경 불가)
        if (orderDTO.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("완료된 주문의 상태는 변경할 수 없습니다.");
        }

        // 3. 비즈니스 로직에 따른 추가 검증
        // 예: 생산 요청(PROCESSING)으로 바꿀 때는 결제 대기(PENDING) 상태여야만 함
        if (newStatus == OrderStatus.PROCESSING && orderDTO.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("결제 대기 상태일 때만 상품 준비 중으로 변경 가능합니다.");
        }

        // 4. 상태 업데이트
        orderDTO.setOrderStatus(newStatus);
        orderRepository.updateOrder(orderDTO);
    }

    @Override
    @Transactional
    public void updateOrderStatusesBasedOnInventory() {
        log.info("Updating order statuses based on inventory...");

        // 1. 모든 미완료 수주 조회 (납기예정일 순)
        List<OrderDTO> uncompletedOrders = orderRepository.getUncompletedOrders();
        if (uncompletedOrders.isEmpty()) return;

        // 2. 현재고 현황 조회 및 Map 변환
        List<Object[]> inventoryData = inventoryRepository.sumCurrentQuantityGrouped();
        Map<Long, Long> inventoryMap = new HashMap<>();
        for (Object[] row : inventoryData) {
            inventoryMap.put((Long) row[0], (Long) row[1]);
        }

        // 3. 수주별 재고 할당 시뮬레이션
        for (OrderDTO order : uncompletedOrders) {
            List<OrderDetailDTO> details = orderRepository.getOrderDetails(order.getOrderId());
            boolean canDeliverAll = true;

            // 해당 주문의 모든 품목이 재고가 충분한지 확인
            for (OrderDetailDTO detail : details) {
                Long availableQty = inventoryMap.getOrDefault(detail.getItemId(), 0L);
                if (availableQty < detail.getQuantity()) {
                    canDeliverAll = false;
                    break;
                }
            }

            OrderStatus targetStatus = canDeliverAll ? OrderStatus.READY : OrderStatus.NEED_PRODUCTION;

            // 재고가 충분한 경우에만 재고 맵에서 수량 차감 (선착순 할당)
            if (canDeliverAll) {
                for (OrderDetailDTO detail : details) {
                    Long currentVal = inventoryMap.get(detail.getItemId());
                    inventoryMap.put(detail.getItemId(), currentVal - detail.getQuantity());
                }
            }

            // 상태가 변경되어야 하는 경우에만 업데이트
            if (order.getOrderStatus() != targetStatus) {
                log.info("Order ID {}: Status changed from {} to {}", order.getOrderId(), order.getOrderStatus(), targetStatus);
                order.setOrderStatus(targetStatus);
                orderRepository.updateOrder(order);
            }
        }
    }

    @Override
    @Transactional
    public void deliveryOrder(Long orderId) {
        // 1. 주문 조회
        OrderDTO order = orderRepository.getOrder(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. ID: " + orderId));

        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("이미 납품 완료된 주문입니다.");
        }

        // 2. 주문 상세 내역 조회
        List<OrderDetailDTO> details = orderRepository.getOrderDetails(orderId);

        // 3. 실제 재고 차감 로직
        for (OrderDetailDTO detail : details) {
            Long remainingToDeduct = detail.getQuantity();
            
            // 유통기한이 빠른 순서대로 해당 품목의 재고를 가져옴
            List<InventoryEntity> inventoryList = 
                    inventoryRepository.findByItemItemIdOrderByExpirationDateAsc(detail.getItemId());

            for (InventoryEntity inventory : inventoryList) {
                if (remainingToDeduct <= 0) break;

                Long currentQty = inventory.getCurrentQuantity();
                if (currentQty <= 0) continue;

                if (currentQty >= remainingToDeduct) {
                    inventory.setCurrentQuantity(currentQty - remainingToDeduct);
                    remainingToDeduct = 0L;
                } else {
                    remainingToDeduct -= currentQty;
                    inventory.setCurrentQuantity(0L);
                }
            }

            if (remainingToDeduct > 0) {
                throw new IllegalStateException("품목 " + detail.getItemName() + "의 재고가 부족합니다.");
            }
            
            // 변경된 재고들 저장 (Dirty checking에 의해 자동 저장되나 명시적으로 해도 좋음)
            inventoryRepository.saveAll(inventoryList);
        }

        // 4. 주문 상태를 납품완료로 변경
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.updateOrder(order);
        
        log.info("Order ID {}: Delivery completed and inventory deducted.", orderId);
    }
}
