package com.itwillbs.ilkwangtech.messenger.entity;

import java.io.Serializable;
import java.util.Objects;

public class MemberFavoriteId implements Serializable {
	private Long memberId;
    private Long targetId;

    public MemberFavoriteId() {}

    public MemberFavoriteId(Long memberId, Long targetId) {
        this.memberId = memberId;
        this.targetId = targetId;
    }

    // equals & hashCode (복합키 비교를 위해 필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberFavoriteId that = (MemberFavoriteId) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, targetId);
    }
}
