package com.itwillbs.ilkwangtech.sales.service.order;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
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

    @Override
    public OrderDTO getOrder(Long id) {
        return orderRepository.getOrder(id).orElseThrow();
    }

    @Override
    public void update(OrderDTO orderDTO) {
        orderRepository.updateOrder(orderDTO);
    }

    @Override
    public boolean isEditable(Long id) {
        OrderDTO order = orderRepository.getOrder(id).orElseThrow();
        if (order == null) {
            return false;
        }

        return order.getOrderStatus() == OrderStatus.PENDING;
    }

    @Override
    public void invalid(Long id) {
        OrderDTO orderDTO = orderRepository.getOrder(id).orElseThrow();
        orderDTO.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.updateOrder(orderDTO);
    }

    @Transactional
    public void changeOrderStatus(Long id, OrderStatus newStatus) {
        // 1. 기존 주문 조회
        OrderDTO orderDTO = orderRepository.getOrder(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 수주 건이 존재하지 않습니다."));

        // 2. 상태 변경 로직 검증 (예: 배송 완료된 건은 상태 변경 불가)
        if (orderDTO.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("완료된 주문의 상태는 변경할 수 없습니다.");
        }

        // 3. 비즈니스 로직에 따른 추가 검증
        // 예: 생산 요청(PROCESSING)으로 바꿀 때는 결제 대기(PENDING) 상태여야만 함
        if (newStatus == OrderStatus.PROCESSING && orderDTO.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("결제 대기 상태일 때만 상품 준비 중으로 변경 가능합니다.");
        }

        // 4. 상태 업데이트
        orderDTO.setOrderStatus(newStatus);
        orderRepository.updateOrder(orderDTO);
    }

}
