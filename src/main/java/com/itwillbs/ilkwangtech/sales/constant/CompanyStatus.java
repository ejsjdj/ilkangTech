package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyStatus implements BaseEnum {

    ACTIVE(1, "거래중"),
    INACTIVE(0, "비활성화");

    private final int code;
    private final String description;

    @Override
    public int getCode() { return code; }

    @Override
    public String getDescription() { return description; }

    public static CompanyStatus fromCode(Integer code) {
        if (code == null) return null;
        for (CompanyStatus status : CompanyStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static CompanyStatus fromDescription(String description) {
        for (CompanyStatus status : CompanyStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("일치하는 상태 설명이 없습니다: " + description);
    }

}