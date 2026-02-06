package com.itwillbs.ilkwangtech.account.constant;

import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {

    ACTIVE(1, "재직중"),
    INACTIVE(2, "휴직중"),
    LEAVE(3, "퇴사");

    private final int code;
    private final String description;

    public static MemberStatus fromCode(Integer code) {
        return Arrays.stream(MemberStatus.values())
                .filter(v -> v.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상태를 알수 없습니다."));
    }

}
