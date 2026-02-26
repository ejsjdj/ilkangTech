package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseOrderStatus {
    CONFIRMED(10, "확정"),
    SHIPPING(20, " 출하"),
    INSPECT(30, "검수"),
    RECEIVE(40, "입고");

    private final int code;
    private final String description;
}
