package com.itwillbs.ilkwangtech.sales.controller;

import com.itwillbs.ilkwangtech.sales.dto.SalesOrderDTO;
import com.itwillbs.ilkwangtech.sales.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sales/sales-order")
@RequiredArgsConstructor
/**
 * 판매 주문을 관리하는 컨트롤러
 */
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    // 기본 CRUD
    @GetMapping
    public ResponseEntity<List<SalesOrderDTO>> getAllSalesOrders() {
        List<SalesOrderDTO> salesOrders = salesOrderService.getAllSalesOrders();
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderDTO> getSalesOrderById(@PathVariable Long id) {
        SalesOrderDTO salesOrder = salesOrderService.getSalesOrderById(id);
        return ResponseEntity.ok(salesOrder);
    }

    @PostMapping
    public ResponseEntity<SalesOrderDTO> createSalesOrder(@RequestBody SalesOrderDTO salesOrderDTO) {
        SalesOrderDTO createdSalesOrder = salesOrderService.createSalesOrder(salesOrderDTO);
        return ResponseEntity.ok(createdSalesOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderDTO> updateSalesOrder(@PathVariable Long id, @RequestBody SalesOrderDTO salesOrderDTO) {
        SalesOrderDTO updatedSalesOrder = salesOrderService.updateSalesOrder(id, salesOrderDTO);
        return ResponseEntity.ok(updatedSalesOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id) {
        salesOrderService.deleteSalesOrder(id);
        return ResponseEntity.ok().build();
    }

    // 판매 요청 관리
    @PostMapping("/request")
    public ResponseEntity<SalesOrderDTO> createSalesRequest(@RequestBody SalesOrderDTO salesOrderDTO) {
        SalesOrderDTO salesRequest = salesOrderService.createSalesRequest(salesOrderDTO);
        return ResponseEntity.ok(salesRequest);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<SalesOrderDTO> acceptSalesOrder(@PathVariable Long id, 
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deliveryDeadline,
                                                         @RequestParam(required = false) String notes) {
        SalesOrderDTO salesOrder = salesOrderService.acceptSalesOrder(id, deliveryDeadline, notes);
        return ResponseEntity.ok(salesOrder);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<SalesOrderDTO> rejectSalesOrder(@PathVariable Long id, @RequestParam String reason) {
        SalesOrderDTO salesOrder = salesOrderService.rejectSalesOrder(id, reason);
        return ResponseEntity.ok(salesOrder);
    }

    @PostMapping("/{id}/start-processing")
    public ResponseEntity<SalesOrderDTO> startProcessing(@PathVariable Long id) {
        SalesOrderDTO salesOrder = salesOrderService.startProcessing(id);
        return ResponseEntity.ok(salesOrder);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<SalesOrderDTO> completeSalesOrder(@PathVariable Long id) {
        SalesOrderDTO salesOrder = salesOrderService.completeSalesOrder(id);
        return ResponseEntity.ok(salesOrder);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SalesOrderDTO> cancelSalesOrder(@PathVariable Long id, @RequestParam String reason) {
        SalesOrderDTO salesOrder = salesOrderService.cancelSalesOrder(id, reason);
        return ResponseEntity.ok(salesOrder);
    }

    // 조회 기능
    @GetMapping("/requested")
    public ResponseEntity<List<SalesOrderDTO>> getRequestedOrders() {
        List<SalesOrderDTO> salesOrders = salesOrderService.getRequestedOrders();
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<SalesOrderDTO>> getOverdueOrders() {
        List<SalesOrderDTO> salesOrders = salesOrderService.getOverdueOrders();
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SalesOrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<SalesOrderDTO> salesOrders = salesOrderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/salesperson/{salesPerson}")
    public ResponseEntity<List<SalesOrderDTO>> getOrdersBySalesPerson(@PathVariable String salesPerson) {
        List<SalesOrderDTO> salesOrders = salesOrderService.getOrdersBySalesPerson(salesPerson);
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/delivery-deadline")
    public ResponseEntity<List<SalesOrderDTO>> getOrdersByDeliveryDeadline(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SalesOrderDTO> salesOrders = salesOrderService.getOrdersByDeliveryDeadline(startDate, endDate);
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/search/order-number")
    public ResponseEntity<List<SalesOrderDTO>> searchByOrderNumber(@RequestParam String orderNumber) {
        List<SalesOrderDTO> salesOrders = salesOrderService.searchByOrderNumber(orderNumber);
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/search/customer-name")
    public ResponseEntity<List<SalesOrderDTO>> searchByCustomerName(@RequestParam String customerName) {
        List<SalesOrderDTO> salesOrders = salesOrderService.searchByCustomerName(customerName);
        return ResponseEntity.ok(salesOrders);
    }

}
