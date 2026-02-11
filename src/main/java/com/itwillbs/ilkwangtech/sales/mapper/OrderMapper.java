package com.itwillbs.ilkwangtech.sales.mapper;

import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper {

    void insertOrder(OrderDTO orderDTO);

    void insertOrderDetails(@Param("list") List<OrderDetailDTO> list);

    List<OrderDTO> selectByPage(@Param("offset") long offset, @Param("pageSize") int pageSize);

    Optional<OrderDTO> selectById(Long id);

    void updateOrder(OrderDTO orderDTO);
}
