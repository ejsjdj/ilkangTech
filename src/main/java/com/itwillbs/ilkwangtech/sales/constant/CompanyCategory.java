package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyCategory implements BaseEnum {

    supplier(1, "협력사"),
    client(2, "고객사"),
    partner(3, "협력사/고객사");

    private final int code;
    private final String description;

    @Override
    public int getCode() { return code; }

    @Override
    public String getDescription() { return description; }

    public static CompanyCategory fromCode(Integer code) {
        if (code == null) return null;
        for (CompanyCategory status : CompanyCategory.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static CompanyCategory fromDescription(String description) {
        for (CompanyCategory status : CompanyCategory.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("일치하는 상태 설명이 없습니다: " + description);
    }

}