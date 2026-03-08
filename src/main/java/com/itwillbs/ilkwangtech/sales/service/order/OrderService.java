package com.itwillbs.ilkwangtech.sales.service.order;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import com.itwillbs.ilkwangtech.sales.mapper.OrderMapper;
import com.itwillbs.ilkwangtech.sales.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    @Transactional
    public List<OrderDTO> getOrderList(OrderStatus status, Pageable pageable) {
        return orderMapper.selectByPageAndStatus(
                status,
                pageable.getOffset(),
                pageable.getPageSize()
        );
    }
}
