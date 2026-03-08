package com.itwillbs.ilkwangtech.sales.service.order;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void createOrder(OrderDTO orderDTO);

    List<OrderDTO> getOrderList(Pageable pageable);

    OrderDTO getOrder(Long id);

    boolean isEditable(Long id);

    void update(OrderDTO orderDTO);

    void invalid(Long id);

    void changeOrderStatus(Long id, OrderStatus status);

    void updateOrderStatusesBasedOnInventory();

    void deliveryOrder(Long orderId);
}
