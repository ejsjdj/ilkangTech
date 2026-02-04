package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    
    Optional<SalesOrder> findBySalesOrderId(Long id);
    
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    
    List<SalesOrder> findByCustomerCustomerId(Long customerId);
    
    List<SalesOrder> findByStatus(String status);
    
    @Query("SELECT s FROM SalesOrder s WHERE s.deliveryDeadline BETWEEN :startDate AND :endDate")
    List<SalesOrder> findByDeliveryDeadlineBetween(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM SalesOrder s WHERE s.status = 'REQUESTED' ORDER BY s.requestDate ASC")
    List<SalesOrder> findRequestedOrders();
    
    @Query("SELECT s FROM SalesOrder s WHERE s.status = 'ACCEPTED' AND s.deliveryDeadline < :currentDate")
    List<SalesOrder> findOverdueOrders(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT s FROM SalesOrder s WHERE s.salesPerson = :salesPerson")
    List<SalesOrder> findBySalesPerson(@Param("salesPerson") String salesPerson);
    
    @Query("SELECT s FROM SalesOrder s WHERE s.orderNumber LIKE CONCAT('%', :orderNumber, '%')")
    List<SalesOrder> findByOrderNumberContaining(@Param("orderNumber") String orderNumber);
    
    @Query("SELECT s FROM SalesOrder s WHERE s.customer.customerName LIKE CONCAT('%', :customerName, '%')")
    List<SalesOrder> findByCustomerNameContaining(@Param("customerName") String customerName);

    @Query("SELECT COUNT(s) FROM SalesOrder s WHERE CAST(s.requestDate AS string) LIKE CONCAT(:dateStr, '%')")
    long countByRequestDatePrefix(@Param("dateStr") String dateStr);
}
