package com.itwillbs.ilkwangtech.sales.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerStatus implements BaseEnum {

    ACTIVE(1, "거래중"),
    INACTIVE(0, "비활성화");

    private final int code;
    private final String description;

}