package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.OrderDetailStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    private Long orderDetailId;
    private Long orderId;
    private Long itemId;
    private String itemName;
    private String itemCode;
    private Long quantity;
    private Long unitPrice;

    // 제품별 개별 출하 시기
    private LocalDateTime deliveryDate;

    // 개별 품목의 배송 상태 (준비중, 출고완료 등)
    private OrderDetailStatus detailStatus;

}