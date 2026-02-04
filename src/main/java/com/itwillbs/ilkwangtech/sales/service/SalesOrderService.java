package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.SalesOrderDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesOrderService {
    List<SalesOrderDTO> getAllSalesOrders();
    SalesOrderDTO getSalesOrderById(Long id);
    SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO);
    SalesOrderDTO updateSalesOrder(Long id, SalesOrderDTO salesOrderDTO);
    void deleteSalesOrder(Long id);
    
    // 판매 요청 관리
    SalesOrderDTO createSalesRequest(SalesOrderDTO salesOrderDTO);
    SalesOrderDTO acceptSalesOrder(Long id, LocalDateTime deliveryDeadline, String notes);
    SalesOrderDTO rejectSalesOrder(Long id, String reason);
    SalesOrderDTO startProcessing(Long id);
    SalesOrderDTO completeSalesOrder(Long id);
    SalesOrderDTO cancelSalesOrder(Long id, String reason);
    
    // 조회 기능
    List<SalesOrderDTO> getRequestedOrders();
    List<SalesOrderDTO> getOverdueOrders();
    List<SalesOrderDTO> getOrdersByCustomer(Long customerId);
    List<SalesOrderDTO> getOrdersBySalesPerson(String salesPerson);
    List<SalesOrderDTO> getOrdersByDeliveryDeadline(LocalDateTime startDate, LocalDateTime endDate);
    List<SalesOrderDTO> searchByOrderNumber(String orderNumber);
    List<SalesOrderDTO> searchByCustomerName(String customerName);
}
