package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.InvoiceDTO;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllInvoices();
    InvoiceDTO getInvoiceById(Long id);
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);
    InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO);
    void deleteInvoice(Long id);
    InvoiceDTO generateInvoiceFromDelivery(Long deliveryOrderId);
    InvoiceDTO markAsPaid(Long id);
    List<InvoiceDTO> getOverdueInvoices();
    List<InvoiceDTO> getInvoicesByCustomer(Long customerId);
    List<InvoiceDTO> getUnpaidInvoices();
}
