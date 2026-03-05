package com.itwillbs.ilkwangtech.item.constant;

import com.itwillbs.ilkwangtech.sales.constant.BaseEnum;
import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemType {
    RAW(1, "원자재"),
    WIP(2, "재공품"),
    FG(3, "완제품"),
    SEMI(4, "반자재");

    private final int code;      // 숫자 코드
    private final String Description;    // 한글 명칭

    public static ItemType fromCode(Integer code) {
        if (code == null) return null;
        for (ItemType status : ItemType.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static ItemType fromDescription(String description) {
        for (ItemType status : ItemType.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("일치하는 상태 설명이 없습니다: " + description);
    }
}