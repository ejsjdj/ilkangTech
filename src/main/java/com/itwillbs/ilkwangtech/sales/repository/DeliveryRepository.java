package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryOrder, Long> {
    
    Optional<DeliveryOrder> findByDeliveryOrderId(Long id);
    
    List<DeliveryOrder> findByStatus(String status);
    
    List<DeliveryOrder> findByCustomerCustomerId(Long customerId);
    
    @Query("SELECT d FROM DeliveryOrder d WHERE d.orderDate BETWEEN :startDate AND :endDate")
    List<DeliveryOrder> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT d FROM DeliveryOrder d WHERE d.status = 'PENDING' ORDER BY d.orderDate ASC")
    List<DeliveryOrder> findPendingDeliveries();
    
    @Query("SELECT d FROM DeliveryOrder d WHERE d.status IN ('PENDING', 'IN_PROGRESS')")
    List<DeliveryOrder> findActiveDeliveries();
}
