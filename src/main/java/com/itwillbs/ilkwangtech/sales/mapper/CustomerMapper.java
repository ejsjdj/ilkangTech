package com.itwillbs.ilkwangtech.sales.mapper;

import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CustomerMapper {


    void insertCustomer(CustomerDTO customerDTO);

    CustomerDTO selectByName(String name);

    List<CustomerDTO> selectByPage(@Param("offset") long offset, @Param("pageSize") int pageSize);

    long selectCount();

    Optional<CustomerDTO> selectById(Long customerId);

    void update(CustomerDTO customerDTO);

    void invalid(@Param("customerId") Long customerId, @Param("status") String status);

    void valid(@Param("customerId") Long customerId, @Param("status") String status);
}
