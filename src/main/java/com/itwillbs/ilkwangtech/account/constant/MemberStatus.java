package com.itwillbs.ilkwangtech.account.constant;

public enum MemberStatus {
    EMPLOYED(1, "재직"),
    WORKING(2, "근무중"),
    OFF_WORK(3, "퇴근"),
    OUT_OFFICE(4, "외근"),
    ANNUAL_LEAVE(5, "연차"),
    RESIGNED(6, "퇴사");

    private final int code;
    private final String description;

    MemberStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }

    // 숫자를 넣으면 해당 Enum을 찾아주는 메서드
    public static MemberStatus fromCode(Integer code) {
        if (code == null) return null;
        for (MemberStatus status : MemberStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    // 설명(String)으로 Enum을 찾아주는 메서드
    public static MemberStatus fromDescription(String description) {
        for (MemberStatus status : MemberStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("일치하는 상태 설명이 없습니다: " + description);
    }
}