package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerId(Long id);
    
    List<Customer> findByCustomerNameContaining(@Param("customerName") String customerName);
    
    List<Customer> findByBusinessNumberContaining(@Param("businessNumber") String businessNumber);
    
    List<Customer> findByContactPersonContaining(@Param("contactPerson") String contactPerson);
}
