package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus implements BaseEnum {
    PENDING(10, "결제대기"),
    PROCESSING(20, "상품준비중"),
    SHIPPING(30, "배송중"),
    COMPLETED(40, "주문완료"),
    CANCELED(90, "주문취소");

    private final int code;
    private final String description;
}
