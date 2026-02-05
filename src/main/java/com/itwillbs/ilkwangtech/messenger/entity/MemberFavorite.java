package com.itwillbs.ilkwangtech.messenger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "member_favorite") // 요청하신 테이블명
@IdClass(MemberFavoriteId.class)
public class MemberFavorite {

    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Id
    @Column(name = "TARGET_ID")
    private Long targetId;

    public MemberFavorite() {}

    public MemberFavorite(Long memberId, Long targetId) {
        this.memberId = memberId;
        this.targetId = targetId;
    }

    // Getter, Setter 생략
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
}
