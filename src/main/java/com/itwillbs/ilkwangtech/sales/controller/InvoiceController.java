package com.itwillbs.ilkwangtech.sales.controller;

import com.itwillbs.ilkwangtech.sales.dto.InvoiceDTO;
import com.itwillbs.ilkwangtech.sales.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales/invoice")
@RequiredArgsConstructor
/**
 * 출하가 완료된 경우 인보이스를 발행해서
 * 회계팀으로 넘긴다
 */
public class InvoiceController {

    private final InvoiceService invoiceService;

    // 전체 인보이스 목록 조회
    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    // 특정 인보이스 조회
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long id) {
        InvoiceDTO invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    // 새로운 인보이스 생성
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO createdInvoice = invoiceService.createInvoice(invoiceDTO);
        return ResponseEntity.ok(createdInvoice);
    }

    // 인보이스 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
        return ResponseEntity.ok(updatedInvoice);
    }

    // 인보이스 삭제 (미지급 상태만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }

    // 배송완료로부터 인보이스 생성
    @PostMapping("/generate/{deliveryOrderId}")
    public ResponseEntity<InvoiceDTO> generateInvoiceFromDelivery(@PathVariable Long deliveryOrderId) {
        InvoiceDTO invoice = invoiceService.generateInvoiceFromDelivery(deliveryOrderId);
        return ResponseEntity.ok(invoice);
    }

    // 인보이스 지급 완료 처리
    @PostMapping("/{id}/paid")
    public ResponseEntity<InvoiceDTO> markAsPaid(@PathVariable Long id) {
        InvoiceDTO invoice = invoiceService.markAsPaid(id);
        return ResponseEntity.ok(invoice);
    }

    // 미지급 인보이스 목록 조회
    @GetMapping("/unpaid")
    public ResponseEntity<List<InvoiceDTO>> getUnpaidInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getUnpaidInvoices();
        return ResponseEntity.ok(invoices);
    }

    // 연체 인보이스 목록 조회
    @GetMapping("/overdue")
    public ResponseEntity<List<InvoiceDTO>> getOverdueInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getOverdueInvoices();
        return ResponseEntity.ok(invoices);
    }

    // 특정 고객의 인보이스 목록 조회
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByCustomer(@PathVariable Long customerId) {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesByCustomer(customerId);
        return ResponseEntity.ok(invoices);
    }

}
