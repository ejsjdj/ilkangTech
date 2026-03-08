package com.itwillbs.ilkwangtech.sales.mapper;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderDTO> selectByPageAndStatus(@Param("status")OrderStatus status, @Param("offset") long offset, @Param("pageSize") int pageSize);

}
