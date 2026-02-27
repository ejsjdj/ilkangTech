package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderDetailStatus implements BaseEnum {
    READY(10, "출고준비"),
    PARTIAL_SHIPPED(20, "부분출고"), // 여러 번 나눠 나갈 경우
    SHIPPED(30, "출고완료"),
    DELIVERED(40, "배송완료"),
    RETURNED(90, "반품접수");

    private final int code;
    private final String description;

    @Override
    public int getCode() { return code; }

    @Override
    public String getDescription() { return description; }

    public static OrderDetailStatus fromCode(Integer code) {
        if (code == null) return null;
        for (OrderDetailStatus status : OrderDetailStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static OrderDetailStatus fromDescription(String description) {
        for (OrderDetailStatus status : OrderDetailStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("일치하는 상태 설명이 없습니다: " + description);
    }
}