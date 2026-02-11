package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerCategory implements BaseEnum {

    supplier(1, "협력사"),
    client(2, "고객사"),
    partner(3, "협력사/고객사");

    private final int code;
    private final String description;

}