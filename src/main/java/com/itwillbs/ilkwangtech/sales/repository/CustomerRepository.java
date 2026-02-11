package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import com.itwillbs.ilkwangtech.sales.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final CustomerMapper customerMapper;

    public void save(CustomerDTO customerDTO) {
        customerMapper.insertCustomer(customerDTO);
    }

    public CustomerDTO findByName(String name) {
        return customerMapper.selectByName(name);
    }

    public List<CustomerDTO> findAll(long offset, int pageSize) {
        return customerMapper.selectByPage(offset, pageSize);
    }

    public long count() {
        return customerMapper.selectCount();
    }

    public Optional<CustomerDTO> findById(Long customerId) {
        return customerMapper.selectById(customerId);
    }

    public void update(CustomerDTO customerDTO) {
        customerMapper.update(customerDTO);
    }

    public void invalid(Long customerId) {
        customerMapper.invalid(customerId);
    }

    public void valid(Long customerId) {
        customerMapper.valid(customerId);
    }

}