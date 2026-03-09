package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long orderId;
    private Long companyId;
    private String companyName;
    private LocalDateTime orderDate; // 주문 접수 일자
    private LocalDateTime expectedDeliveryDate; // 납기 예정 일자
    private OrderStatus orderStatus;      // 주문 상태 (접수, 처리중, 완료 등)
    private String orderStatusDescription; // 주문 상태 한글명
    private List<OrderDetailDTO> orderDetails;
    private Long totalAmount;

}
