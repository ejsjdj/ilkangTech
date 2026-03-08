package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus implements BaseEnum {
    PENDING(10, "결제대기"),
    READY(15, "납기가능"),
    NEED_PRODUCTION(16, "생산필요"),
    PROCESSING(20, "상품준비중"),
    SHIPPING(30, "배송중"),
    COMPLETED(40, "주문완료"),
    CANCELED(90, "주문취소");

    private final int code;
    private final String description;

    @Override
    public int getCode() { return code; }

    @Override
    public String getDescription() { return description; }

    public static OrderStatus fromCode(Integer code) {
        if (code == null) return null;
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static OrderStatus fromDescription(String description) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("일치하는 상태 설명이 없습니다: " + description);
    }
}
