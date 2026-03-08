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
    private LocalDateTime orderDate;
    private LocalDateTime deliveryTime;
    private OrderStatus orderStatus;
    private List<OrderDetailDTO> orderDetails;

}
