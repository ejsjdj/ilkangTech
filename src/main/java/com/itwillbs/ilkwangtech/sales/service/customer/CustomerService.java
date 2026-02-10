package com.itwillbs.ilkwangtech.sales.service.customer;

import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    void createCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getCustomerList(Pageable pageable);

    long getTotalCount();

    CustomerDTO getCustomerById(Long id);

    void updateCustomer(Long id, CustomerDTO customerDTO);

    void invalidCustomer(Long id);

    void validCustomer(Long id);
}
