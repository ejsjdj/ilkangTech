package com.itwillbs.ilkwangtech.sales.service.order;

import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void createOrder(OrderDTO orderDTO) {

        orderRepository.saveOrderDTO(orderDTO);

        Long generatedOrderId = orderDTO.getOrderId();
        List<OrderDetailDTO> orderDetails = orderDTO.getOrderDetails();

        for (OrderDetailDTO detail : orderDetails) {
            detail.setOrderId(generatedOrderId);
        }

        orderRepository.saveOrderDetail(orderDetails);
    }


    @Override
    public List<OrderDTO> getOrderList(Pageable pageable) {
        return orderRepository.getOrderList(
                pageable.getOffset(),
                pageable.getPageSize()
        );
    }

}
