package com.itwillbs.ilkwangtech.sales.service.order;

import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void createOrder(OrderDTO orderDTO);

    List<OrderDTO> getOrderList(Pageable pageable);
}
