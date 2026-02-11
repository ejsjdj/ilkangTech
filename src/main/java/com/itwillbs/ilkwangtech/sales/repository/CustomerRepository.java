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
        // 매퍼 인터페이스에 @Param("offset"), @Param("pageSize")가 있어야 합니다.
        return customerMapper.selectByPage(offset, pageSize);
    }

    public long count() {
        return customerMapper.selectCount(); // 인터페이스 메서드명이 selectCount라면 수정 필요
    }

    public Optional<CustomerDTO> findById(Long customerId) {
        return customerMapper.selectById(customerId);
    }

    public void update(CustomerDTO customerDTO) {
        customerMapper.update(customerDTO);
    }

    public void invalid(Long customerId) {
        // XML의 #{status}에 들어갈 값을 함께 넘겨줘야 에러가 안 납니다.
        customerMapper.invalid(customerId, "N");
    }

    public void valid(Long customerId) {
        customerMapper.valid(customerId, "Y");
    }
}
