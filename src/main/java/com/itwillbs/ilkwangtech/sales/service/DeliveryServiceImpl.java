package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.DeliveryDTO;
import com.itwillbs.ilkwangtech.sales.entity.DeliveryOrder;
import com.itwillbs.ilkwangtech.sales.entity.Customer;
import com.itwillbs.ilkwangtech.sales.entity.ItemStock;
import com.itwillbs.ilkwangtech.sales.repository.DeliveryRepository;
import com.itwillbs.ilkwangtech.sales.repository.CustomerRepository;
import com.itwillbs.ilkwangtech.sales.repository.ItemStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;
    private final ItemStockRepository itemStockRepository;
    private final InvoiceService invoiceService;

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getAllDeliveries() {
        List<DeliveryOrder> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDTO getDeliveryById(Long id) {
        DeliveryOrder delivery = deliveryRepository.findByDeliveryOrderId(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + id));
        return convertToDTO(delivery);
    }

    @Override
    @Transactional
    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        DeliveryOrder delivery = convertToEntity(deliveryDTO);
        delivery.setStatus("PENDING");
        delivery.setOrderDate(LocalDateTime.now());
        
        // 재고 확인
        ItemStock itemStock = delivery.getItemStock();
        if (itemStock.getQuantity() < delivery.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + itemStock.getQuantity() + 
                                     ", Requested: " + delivery.getQuantity());
        }
        
        DeliveryOrder savedDelivery = deliveryRepository.save(delivery);
        return convertToDTO(savedDelivery);
    }

    @Override
    @Transactional
    public DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO) {
        DeliveryOrder existingDelivery = deliveryRepository.findByDeliveryOrderId(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + id));
        
        updateDeliveryFromDTO(existingDelivery, deliveryDTO);
        DeliveryOrder updatedDelivery = deliveryRepository.save(existingDelivery);
        return convertToDTO(updatedDelivery);
    }

    @Override
    @Transactional
    public void deleteDelivery(Long id) {
        DeliveryOrder delivery = deliveryRepository.findByDeliveryOrderId(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + id));
        
        // 진행 중이거나 완료된 배송은 삭제 불가
        if ("IN_PROGRESS".equals(delivery.getStatus()) || "COMPLETED".equals(delivery.getStatus())) {
            throw new RuntimeException("Cannot delete delivery that is in progress or completed");
        }
        
        deliveryRepository.delete(delivery);
    }

    @Override
    @Transactional
    public DeliveryDTO startDelivery(Long id) {
        DeliveryOrder delivery = deliveryRepository.findByDeliveryOrderId(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + id));
        
        if (!"PENDING".equals(delivery.getStatus())) {
            throw new RuntimeException("Delivery must be in PENDING status to start");
        }
        
        delivery.setStatus("IN_PROGRESS");
        delivery.setDeliveryDate(LocalDateTime.now());
        
        // 재고 감소
        ItemStock itemStock = delivery.getItemStock();
        if (itemStock.getQuantity() < delivery.getQuantity()) {
            throw new RuntimeException("Insufficient stock for delivery");
        }
        itemStock.setQuantity(itemStock.getQuantity() - delivery.getQuantity());
        itemStockRepository.save(itemStock);
        
        DeliveryOrder updatedDelivery = deliveryRepository.save(delivery);
        return convertToDTO(updatedDelivery);
    }

    @Override
    @Transactional
    public DeliveryDTO completeDelivery(Long id) {
        DeliveryOrder delivery = deliveryRepository.findByDeliveryOrderId(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + id));
        
        if (!"IN_PROGRESS".equals(delivery.getStatus())) {
            throw new RuntimeException("Delivery must be in IN_PROGRESS status to complete");
        }
        
        delivery.setStatus("COMPLETED");
        delivery.setCompletedDate(LocalDateTime.now());
        
        // 인보이스 자동 발행
        try {
            invoiceService.generateInvoiceFromDelivery(id);
            System.out.println("인보이스가 자동으로 발행되었습니다. 배송주문번호: " + id);
        } catch (Exception e) {
            // 인보이스 발행 실패 시 로그만 남기고 배송 완료는 진행
            System.err.println("인보이스 자동 발행 실패: " + e.getMessage());
            throw new RuntimeException("배송 완료 처리 중 인보이스 발행에 실패했습니다: " + e.getMessage());
        }
        
        DeliveryOrder updatedDelivery = deliveryRepository.save(delivery);
        return convertToDTO(updatedDelivery);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getPendingDeliveries() {
        List<DeliveryOrder> deliveries = deliveryRepository.findPendingDeliveries();
        return deliveries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getActiveDeliveries() {
        List<DeliveryOrder> deliveries = deliveryRepository.findActiveDeliveries();
        return deliveries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveriesByCustomer(Long customerId) {
        List<DeliveryOrder> deliveries = deliveryRepository.findByCustomerCustomerId(customerId);
        return deliveries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DeliveryDTO convertToDTO(DeliveryOrder delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setDeliveryOrderId(delivery.getDeliveryOrderId());
        dto.setCustomerId(delivery.getCustomer() != null ? delivery.getCustomer().getCustomerId() : null);
        dto.setCustomerName(delivery.getCustomer() != null ? delivery.getCustomer().getCustomerName() : null);
        dto.setItemStockId(delivery.getItemStock() != null ? delivery.getItemStock().getItemStockId() : null);
        dto.setProductName(delivery.getItemStock() != null ? delivery.getItemStock().getProductName() : null);
        dto.setWarehouseName(delivery.getItemStock() != null && delivery.getItemStock().getWarehouse() != null ?
                            delivery.getItemStock().getWarehouse().getWarehouseName() : null);
        dto.setQuantity(delivery.getQuantity());
        dto.setStatus(delivery.getStatus());
        dto.setOrderDate(delivery.getOrderDate());
        dto.setDeliveryDate(delivery.getDeliveryDate());
        dto.setCompletedDate(delivery.getCompletedDate());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setNotes(delivery.getNotes());
        return dto;
    }

    private DeliveryOrder convertToEntity(DeliveryDTO dto) {
        DeliveryOrder delivery = new DeliveryOrder();
        delivery.setDeliveryOrderId(dto.getDeliveryOrderId());
        
        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerId(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            delivery.setCustomer(customer);
        }
        
        if (dto.getItemStockId() != null) {
            ItemStock itemStock = itemStockRepository.findByItemStockId(dto.getItemStockId())
                    .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + dto.getItemStockId()));
            delivery.setItemStock(itemStock);
        }
        
        delivery.setQuantity(dto.getQuantity());
        delivery.setStatus(dto.getStatus());
        delivery.setOrderDate(dto.getOrderDate());
        delivery.setDeliveryDate(dto.getDeliveryDate());
        delivery.setCompletedDate(dto.getCompletedDate());
        delivery.setDeliveryAddress(dto.getDeliveryAddress());
        delivery.setTrackingNumber(dto.getTrackingNumber());
        delivery.setNotes(dto.getNotes());
        return delivery;
    }

    private void updateDeliveryFromDTO(DeliveryOrder delivery, DeliveryDTO dto) {
        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerId(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            delivery.setCustomer(customer);
        }
        
        if (dto.getItemStockId() != null) {
            ItemStock itemStock = itemStockRepository.findByItemStockId(dto.getItemStockId())
                    .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + dto.getItemStockId()));
            delivery.setItemStock(itemStock);
        }
        
        delivery.setQuantity(dto.getQuantity());
        delivery.setStatus(dto.getStatus());
        delivery.setDeliveryAddress(dto.getDeliveryAddress());
        delivery.setTrackingNumber(dto.getTrackingNumber());
        delivery.setNotes(dto.getNotes());
    }
}
