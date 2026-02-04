package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;

@Getter
public enum QuotationStatus {
    DRAFT("임시저장"),
    SENT("발송됨"),
    ACCEPTED("수락됨"),
    REJECTED("거절됨"),
    EXPIRED("만료됨");

    private final String description;

    QuotationStatus(String description) {
        this.description = description;
    }
}
