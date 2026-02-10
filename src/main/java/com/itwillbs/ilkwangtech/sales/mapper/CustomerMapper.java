package com.itwillbs.ilkwangtech.sales.mapper;

import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerMapper {


    void insertCustomer(CustomerDTO customerDTO);

    CustomerDTO selectByName(String name);

    List<CustomerDTO> selectByPage(long offset, int pageSize);

    long selectCount();

    Optional<CustomerDTO> selectById(Long customerId);

    void update(CustomerDTO customerDTO);

    void invalid(Long customerId);

    void valid(Long customerId);
}
