package com.itwillbs.ilkwangtech.item.constant;

import com.itwillbs.ilkwangtech.sales.constant.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemType implements BaseEnum {
    RAW(1, "원자재"),
    WIP(2, "재공품"),
    FG(3, "완제품");

    private final int code;      // 숫자 코드
    private final String Description;    // 한글 명칭

}
