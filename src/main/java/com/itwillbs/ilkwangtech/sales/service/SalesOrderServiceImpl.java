package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.SalesOrderDTO;
import com.itwillbs.ilkwangtech.sales.entity.SalesOrder;
import com.itwillbs.ilkwangtech.sales.entity.Customer;
import com.itwillbs.ilkwangtech.sales.entity.ItemStock;
import com.itwillbs.ilkwangtech.sales.repository.SalesOrderRepository;
import com.itwillbs.ilkwangtech.sales.repository.CustomerRepository;
import com.itwillbs.ilkwangtech.sales.repository.ItemStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final ItemStockRepository itemStockRepository;
    private final DeliveryService deliveryService;
    private final PriceManagementService priceManagementService;

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getAllSalesOrders() {
        List<SalesOrder> salesOrders = salesOrderRepository.findAll();
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderDTO getSalesOrderById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        return convertToDTO(salesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO) {
        SalesOrder salesOrder = convertToEntity(salesOrderDTO);
        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(savedSalesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO updateSalesOrder(Long id, SalesOrderDTO salesOrderDTO) {
        SalesOrder existingSalesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        
        updateSalesOrderFromDTO(existingSalesOrder, salesOrderDTO);
        SalesOrder updatedSalesOrder = salesOrderRepository.save(existingSalesOrder);
        return convertToDTO(updatedSalesOrder);
    }

    @Override
    @Transactional
    public void deleteSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        
        // 진행 중이거나 완료된 주문은 삭제 불가
        if ("IN_PROGRESS".equals(salesOrder.getStatus()) || "COMPLETED".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Cannot delete sales order that is in progress or completed");
        }
        
        salesOrderRepository.delete(salesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO createSalesRequest(SalesOrderDTO salesOrderDTO) {
        SalesOrder salesOrder = new SalesOrder();
        
        // 주문 번호 자동 생성
        String orderNumber = generateOrderNumber();
        salesOrder.setOrderNumber(orderNumber);
        
        // 기본 정보 설정
        if (salesOrderDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerId(salesOrderDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + salesOrderDTO.getCustomerId()));
            salesOrder.setCustomer(customer);
        }
        
        if (salesOrderDTO.getItemStockId() != null) {
            ItemStock itemStock = itemStockRepository.findByItemStockId(salesOrderDTO.getItemStockId())
                    .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + salesOrderDTO.getItemStockId()));
            salesOrder.setItemStock(itemStock);
            
            // 단가가 없는 경우 PriceManagementService를 통해 조회
            if (salesOrderDTO.getUnitPrice() == null) {
                BigDecimal currentPrice = priceManagementService.getCurrentPrice(
                        salesOrderDTO.getItemStockId(), salesOrderDTO.getCustomerId());
                salesOrder.setUnitPrice(currentPrice);
            } else {
                salesOrder.setUnitPrice(salesOrderDTO.getUnitPrice());
            }
        } else {
            salesOrder.setUnitPrice(salesOrderDTO.getUnitPrice());
        }
        
        salesOrder.setQuantity(salesOrderDTO.getQuantity());
        
        // 총액 계산
        if (salesOrder.getQuantity() != null && salesOrder.getUnitPrice() != null) {
            BigDecimal totalAmount = salesOrder.getUnitPrice().multiply(new BigDecimal(salesOrder.getQuantity()));
            salesOrder.setTotalAmount(totalAmount);
        }
        
        salesOrder.setStatus("REQUESTED");
        salesOrder.setRequestDate(LocalDateTime.now());
        salesOrder.setDeliveryDeadline(salesOrderDTO.getDeliveryDeadline());
        salesOrder.setCustomerRequest(salesOrderDTO.getCustomerRequest());
        salesOrder.setSalesPerson(salesOrderDTO.getSalesPerson());
        salesOrder.setContactPerson(salesOrderDTO.getContactPerson());
        salesOrder.setContactPhone(salesOrderDTO.getContactPhone());
        salesOrder.setNotes(salesOrderDTO.getNotes());
        
        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(savedSalesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO acceptSalesOrder(Long id, LocalDateTime deliveryDeadline, String notes) {
        SalesOrder salesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        
        if (!"REQUESTED".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Sales order must be in REQUESTED status to accept");
        }
        
        salesOrder.setStatus("ACCEPTED");
        salesOrder.setAcceptanceDate(LocalDateTime.now());
        salesOrder.setDeliveryDeadline(deliveryDeadline);
        salesOrder.setNotes(notes);
        
        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(updatedSalesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO rejectSalesOrder(Long id, String reason) {
        SalesOrder salesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        
        if (!"REQUESTED".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Sales order must be in REQUESTED status to reject");
        }
        
        salesOrder.setStatus("REJECTED");
        salesOrder.setNotes("거부 사유: " + reason);
        
        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(updatedSalesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO startProcessing(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        
        if (!"ACCEPTED".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Sales order must be in ACCEPTED status to start processing");
        }
        
        salesOrder.setStatus("IN_PROGRESS");
        
        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(updatedSalesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO completeSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        
        if (!"IN_PROGRESS".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Sales order must be in IN_PROGRESS status to complete");
        }
        
        salesOrder.setStatus("COMPLETED");
        salesOrder.setActualDeliveryDate(LocalDateTime.now());
        
        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        
        // 수주 완료 시 자동으로 출고 지시(Delivery) 생성
        try {
            com.itwillbs.ilkwangtech.sales.dto.DeliveryDTO deliveryDTO = new com.itwillbs.ilkwangtech.sales.dto.DeliveryDTO();
            deliveryDTO.setCustomerId(salesOrder.getCustomer().getCustomerId());
            deliveryDTO.setItemStockId(salesOrder.getItemStock().getItemStockId());
            deliveryDTO.setQuantity(salesOrder.getQuantity());
            deliveryDTO.setDeliveryAddress(salesOrder.getCustomer().getAddress()); // 고객 주소 사용
            deliveryDTO.setNotes("수주 자동 생성 - 주문번호: " + salesOrder.getOrderNumber());
            
            deliveryService.createDelivery(deliveryDTO);
        } catch (Exception e) {
            // 출고 지시 생성 실패 시 로그만 남김 (수주 완료는 유지)
            System.err.println("자동 출고 지시 생성 실패: " + e.getMessage());
        }
        
        return convertToDTO(updatedSalesOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO cancelSalesOrder(Long id, String reason) {
        SalesOrder salesOrder = salesOrderRepository.findBySalesOrderId(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));
        
        if ("COMPLETED".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Cannot cancel completed sales order");
        }
        
        salesOrder.setStatus("CANCELLED");
        salesOrder.setNotes("취소 사유: " + reason);
        
        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(updatedSalesOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getRequestedOrders() {
        List<SalesOrder> salesOrders = salesOrderRepository.findRequestedOrders();
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getOverdueOrders() {
        List<SalesOrder> salesOrders = salesOrderRepository.findOverdueOrders(LocalDateTime.now());
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getOrdersByCustomer(Long customerId) {
        List<SalesOrder> salesOrders = salesOrderRepository.findByCustomerCustomerId(customerId);
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getOrdersBySalesPerson(String salesPerson) {
        List<SalesOrder> salesOrders = salesOrderRepository.findBySalesPerson(salesPerson);
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getOrdersByDeliveryDeadline(LocalDateTime startDate, LocalDateTime endDate) {
        List<SalesOrder> salesOrders = salesOrderRepository.findByDeliveryDeadlineBetween(startDate, endDate);
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> searchByOrderNumber(String orderNumber) {
        List<SalesOrder> salesOrders = salesOrderRepository.findByOrderNumberContaining(orderNumber);
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> searchByCustomerName(String customerName) {
        List<SalesOrder> salesOrders = salesOrderRepository.findByCustomerNameContaining(customerName);
        return salesOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 내부 메서드
    private String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = LocalDateTime.now().format(formatter);
        
        long count = salesOrderRepository.countByRequestDatePrefix(dateStr);
        
        String displayDateStr = dateStr.replace("-", "");
        return String.format("SO-%s-%04d", displayDateStr, count + 1);
    }

    private SalesOrderDTO convertToDTO(SalesOrder salesOrder) {
        SalesOrderDTO dto = new SalesOrderDTO();
        dto.setSalesOrderId(salesOrder.getSalesOrderId());
        dto.setCustomerId(salesOrder.getCustomer() != null ? salesOrder.getCustomer().getCustomerId() : null);
        dto.setCustomerName(salesOrder.getCustomer() != null ? salesOrder.getCustomer().getCustomerName() : null);
        dto.setItemStockId(salesOrder.getItemStock() != null ? salesOrder.getItemStock().getItemStockId() : null);
        dto.setProductName(salesOrder.getItemStock() != null ? salesOrder.getItemStock().getProductName() : null);
        dto.setWarehouseName(salesOrder.getItemStock() != null && salesOrder.getItemStock().getWarehouse() != null ?
                            salesOrder.getItemStock().getWarehouse().getWarehouseName() : null);
        dto.setOrderNumber(salesOrder.getOrderNumber());
        dto.setStatus(salesOrder.getStatus());
        dto.setQuantity(salesOrder.getQuantity());
        dto.setUnitPrice(salesOrder.getUnitPrice());
        dto.setTotalAmount(salesOrder.getTotalAmount());
        dto.setRequestDate(salesOrder.getRequestDate());
        dto.setAcceptanceDate(salesOrder.getAcceptanceDate());
        dto.setDeliveryDeadline(salesOrder.getDeliveryDeadline());
        dto.setExpectedDeliveryDate(salesOrder.getExpectedDeliveryDate());
        dto.setActualDeliveryDate(salesOrder.getActualDeliveryDate());
        dto.setCustomerRequest(salesOrder.getCustomerRequest());
        dto.setNotes(salesOrder.getNotes());
        dto.setSalesPerson(salesOrder.getSalesPerson());
        dto.setContactPerson(salesOrder.getContactPerson());
        dto.setContactPhone(salesOrder.getContactPhone());
        return dto;
    }

    private SalesOrder convertToEntity(SalesOrderDTO dto) {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setSalesOrderId(dto.getSalesOrderId());
        salesOrder.setOrderNumber(dto.getOrderNumber());
        salesOrder.setStatus(dto.getStatus());
        salesOrder.setQuantity(dto.getQuantity());
        salesOrder.setUnitPrice(dto.getUnitPrice());
        salesOrder.setTotalAmount(dto.getTotalAmount());
        salesOrder.setRequestDate(dto.getRequestDate());
        salesOrder.setAcceptanceDate(dto.getAcceptanceDate());
        salesOrder.setDeliveryDeadline(dto.getDeliveryDeadline());
        salesOrder.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        salesOrder.setActualDeliveryDate(dto.getActualDeliveryDate());
        salesOrder.setCustomerRequest(dto.getCustomerRequest());
        salesOrder.setNotes(dto.getNotes());
        salesOrder.setSalesPerson(dto.getSalesPerson());
        salesOrder.setContactPerson(dto.getContactPerson());
        salesOrder.setContactPhone(dto.getContactPhone());
        
        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerId(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            salesOrder.setCustomer(customer);
        }
        
        if (dto.getItemStockId() != null) {
            ItemStock itemStock = itemStockRepository.findByItemStockId(dto.getItemStockId())
                    .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + dto.getItemStockId()));
            salesOrder.setItemStock(itemStock);
        }
        
        return salesOrder;
    }

    private void updateSalesOrderFromDTO(SalesOrder salesOrder, SalesOrderDTO dto) {
        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerId(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            salesOrder.setCustomer(customer);
        }
        
        if (dto.getItemStockId() != null) {
            ItemStock itemStock = itemStockRepository.findByItemStockId(dto.getItemStockId())
                    .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + dto.getItemStockId()));
            salesOrder.setItemStock(itemStock);
        }
        
        salesOrder.setQuantity(dto.getQuantity());
        salesOrder.setUnitPrice(dto.getUnitPrice());
        salesOrder.setTotalAmount(dto.getTotalAmount());
        salesOrder.setDeliveryDeadline(dto.getDeliveryDeadline());
        salesOrder.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        salesOrder.setCustomerRequest(dto.getCustomerRequest());
        salesOrder.setNotes(dto.getNotes());
        salesOrder.setSalesPerson(dto.getSalesPerson());
        salesOrder.setContactPerson(dto.getContactPerson());
        salesOrder.setContactPhone(dto.getContactPhone());
    }
}
