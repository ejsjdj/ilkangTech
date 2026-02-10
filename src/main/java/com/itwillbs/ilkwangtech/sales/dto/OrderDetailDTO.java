package com.itwillbs.ilkwangtech.sales.dto;


import com.itwillbs.ilkwangtech.sales.constant.OrderDetailStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderDetailDTO {

    private Long orderDetailId;
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Double price;

    // 제품별 개별 출하 시기
    private LocalDateTime deliveryDate;

    // 개별 품목의 배송 상태 (준비중, 출고완료 등)
    private OrderDetailStatus detailStatus;

}