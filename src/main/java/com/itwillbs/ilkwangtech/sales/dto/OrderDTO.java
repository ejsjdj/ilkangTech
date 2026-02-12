package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDTO {

    private Long orderId;
    private Long CompanyId;
    private LocalDateTime orderDate; // 주문 접수 일자
    private OrderStatus orderStatus;      // 주문 상태 (접수, 처리중, 완료 등)
    private List<OrderDetailDTO> orderDetails;

}
