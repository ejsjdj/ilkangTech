package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.InvoiceDTO;
import com.itwillbs.ilkwangtech.sales.entity.Invoice;
import com.itwillbs.ilkwangtech.sales.entity.DeliveryOrder;
import com.itwillbs.ilkwangtech.sales.entity.Customer;
import com.itwillbs.ilkwangtech.sales.repository.InvoiceRepository;
import com.itwillbs.ilkwangtech.sales.repository.DeliveryRepository;
import com.itwillbs.ilkwangtech.sales.repository.CustomerRepository;
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
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;

    private final PriceManagementService priceManagementService;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% 부가세

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findByInvoiceId(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return convertToDTO(invoice);
    }

    @Override
    @Transactional
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = convertToEntity(invoiceDTO);
        invoice.setStatus("ISSUED");
        invoice.setIssueDate(LocalDateTime.now());
        
        // 자동으로 납기일 설정 (발행일로부터 30일 후)
        invoice.setDueDate(LocalDateTime.now().plusDays(30));
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice existingInvoice = invoiceRepository.findByInvoiceId(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        updateInvoiceFromDTO(existingInvoice, invoiceDTO);
        Invoice updatedInvoice = invoiceRepository.save(existingInvoice);
        return convertToDTO(updatedInvoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findByInvoiceId(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        // 지급된 인보이스는 삭제 불가
        if ("PAID".equals(invoice.getStatus())) {
            throw new RuntimeException("Cannot delete paid invoice");
        }
        
        invoiceRepository.delete(invoice);
    }

    @Override
    @Transactional
    public InvoiceDTO generateInvoiceFromDelivery(Long deliveryOrderId) {
        // 배송 정보 조회
        DeliveryOrder delivery = deliveryRepository.findByDeliveryOrderId(deliveryOrderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + deliveryOrderId));
        
        // 이미 인보이스가 있는지 확인
        if (invoiceRepository.findByDeliveryOrder_DeliveryOrderId(deliveryOrderId).isPresent()) {
            throw new RuntimeException("Invoice already exists for delivery order: " + deliveryOrderId);
        }
        
        // 인보이스 생성
        Invoice invoice = new Invoice();
        invoice.setDeliveryOrder(delivery);
        invoice.setCustomer(delivery.getCustomer());
        
        // 인보이스 번호 생성 (INV-YYYYMMDD-NNNN)
        String invoiceNumber = generateInvoiceNumber();
        invoice.setInvoiceNumber(invoiceNumber);
        
        // 금액 계산 (PriceManagementService를 통해 실제 단가 조회)
        BigDecimal unitPrice = priceManagementService.getCurrentPrice(
                delivery.getItemStock().getItemStockId(), 
                delivery.getCustomer().getCustomerId());
        
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            unitPrice = new BigDecimal("10000"); // 기본값 유지 (필요 시 예외 처리 가능)
        }
        
        BigDecimal totalAmount = unitPrice.multiply(new BigDecimal(delivery.getQuantity()));
        BigDecimal taxAmount = totalAmount.multiply(TAX_RATE);
        BigDecimal finalAmount = totalAmount.add(taxAmount);
        
        invoice.setTotalAmount(totalAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setFinalAmount(finalAmount);
        
        invoice.setStatus("ISSUED");
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setDueDate(LocalDateTime.now().plusDays(30));
        invoice.setBillingAddress(delivery.getDeliveryAddress());
        invoice.setNotes("자동 생성된 인보이스 - 배송주문번호: " + deliveryOrderId);
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceDTO markAsPaid(Long id) {
        Invoice invoice = invoiceRepository.findByInvoiceId(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        if (!"ISSUED".equals(invoice.getStatus())) {
            throw new RuntimeException("Invoice must be in ISSUED status to mark as paid");
        }
        
        invoice.setStatus("PAID");
        invoice.setPaidDate(LocalDateTime.now());
        
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(updatedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getOverdueInvoices() {
        List<Invoice> invoices = invoiceRepository.findOverdueInvoices(LocalDateTime.now());
        return invoices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoicesByCustomer(Long customerId) {
        List<Invoice> invoices = invoiceRepository.findByCustomerCustomerId(customerId);
        return invoices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getUnpaidInvoices() {
        List<Invoice> invoices = invoiceRepository.findByStatus("ISSUED");
        return invoices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private String generateInvoiceNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = LocalDateTime.now().format(formatter);
        
        long count = invoiceRepository.countByIssueDatePrefix(dateStr);
        
        String displayDateStr = dateStr.replace("-", "");
        return String.format("INV-%s-%04d", displayDateStr, count + 1);
    }

    private InvoiceDTO convertToDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setDeliveryOrderId(invoice.getDeliveryOrder() != null ? invoice.getDeliveryOrder().getDeliveryOrderId() : null);
        dto.setCustomerId(invoice.getCustomer() != null ? invoice.getCustomer().getCustomerId() : null);
        dto.setCustomerName(invoice.getCustomer() != null ? invoice.getCustomer().getCustomerName() : null);
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setFinalAmount(invoice.getFinalAmount());
        dto.setStatus(invoice.getStatus());
        dto.setIssueDate(invoice.getIssueDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setPaidDate(invoice.getPaidDate());
        dto.setBillingAddress(invoice.getBillingAddress());
        dto.setNotes(invoice.getNotes());
        return dto;
    }

    private Invoice convertToEntity(InvoiceDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(dto.getInvoiceId());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setTaxAmount(dto.getTaxAmount());
        invoice.setFinalAmount(dto.getFinalAmount());
        invoice.setStatus(dto.getStatus());
        invoice.setIssueDate(dto.getIssueDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setPaidDate(dto.getPaidDate());
        invoice.setBillingAddress(dto.getBillingAddress());
        invoice.setNotes(dto.getNotes());
        
        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerId(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            invoice.setCustomer(customer);
        }
        
        return invoice;
    }

    private void updateInvoiceFromDTO(Invoice invoice, InvoiceDTO dto) {
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setTaxAmount(dto.getTaxAmount());
        invoice.setFinalAmount(dto.getFinalAmount());
        invoice.setStatus(dto.getStatus());
        invoice.setDueDate(dto.getDueDate());
        invoice.setPaidDate(dto.getPaidDate());
        invoice.setBillingAddress(dto.getBillingAddress());
        invoice.setNotes(dto.getNotes());
    }
}
