package com.itwillbs.ilkwangtech.sales.mapper;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
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

    List<OrderDTO> selectByPage(@Param("offset") long offset, @Param("pageSize") int pageSize, @Param("statuses") List<OrderStatus> statuses);

    long countOrders(@Param("statuses") List<OrderStatus> statuses);

    Optional<OrderDTO> selectById(Long id);

    List<OrderDetailDTO> selectDetailsByOrderId(Long orderId);
    
    List<OrderDTO> selectUncompletedOrders();

    List<OrderDTO> selectByCompanyId(@Param("companyId") Long companyId);
    
    List<OrderDTO> selectByCompanyIdPage(@Param("companyId") Long companyId, @Param("offset") long offset, @Param("pageSize") int pageSize);

    long countByCompanyId(@Param("companyId") Long companyId);

    void updateOrder(OrderDTO orderDTO);
}
