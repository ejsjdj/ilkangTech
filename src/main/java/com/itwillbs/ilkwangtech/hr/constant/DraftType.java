package com.itwillbs.ilkwangtech.hr.constant;

import lombok.Getter;

@Getter
public enum DraftType {

    PTO("연차"),
    HTO("반차"),
    EXP("지출"),
    BUY("구매"),
    APP("발령");
    private final String description;

    DraftType(String dratpType) {
        this.description = dratpType;
    }
}
