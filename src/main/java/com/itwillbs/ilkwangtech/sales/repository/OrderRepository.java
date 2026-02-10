package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final OrderMapper orderMapper;

    public void saveOrderDTO(OrderDTO orderDTO) {
        orderMapper.insertOrder(orderDTO);
    }

    public void saveOrderDetail(List<OrderDetailDTO> orderDetails) {
        orderMapper.insertOrderDetail(orderDetails);
    }

    public List<OrderDTO> getOrderList(long offset, int pageSize) {
        return orderMapper.selectByPage(offset, pageSize);
    }

    public Optional<OrderDTO> getOrder(Long id) {
        return orderMapper.selectById(id);
    }

    public void updateOrder(OrderDTO orderDTO) {
        orderMapper.updateOrder(orderDTO);
    }

}
