package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus implements BaseEnum {
    NEED_PRODUCTION(16, "생산필요"),
    READY(15, "준비완료"),
    COMPLETED(40, "납품완료");

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
