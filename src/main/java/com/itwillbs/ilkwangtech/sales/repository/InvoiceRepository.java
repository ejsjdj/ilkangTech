package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    Optional<Invoice> findByInvoiceId(Long id);
    
    Optional<Invoice> findByDeliveryOrder_DeliveryOrderId(Long deliveryOrderId);
    
    List<Invoice> findByCustomerCustomerId(Long customerId);
    
    List<Invoice> findByStatus(String status);
    
    @Query("SELECT i FROM Invoice i WHERE i.issueDate BETWEEN :startDate AND :endDate")
    List<Invoice> findByIssueDateBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT i FROM Invoice i WHERE i.status = 'ISSUED' AND i.dueDate < :currentDate")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT i FROM Invoice i WHERE i.invoiceNumber LIKE CONCAT('%', :invoiceNumber, '%')")
    List<Invoice> findByInvoiceNumberContaining(@Param("invoiceNumber") String invoiceNumber);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE CAST(i.issueDate AS string) LIKE CONCAT(:dateStr, '%')")
    long countByIssueDatePrefix(@Param("dateStr") String dateStr);
}
