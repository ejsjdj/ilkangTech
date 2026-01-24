package com.itwillbs.ilkwangtech.account.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "LOGIN_ATTEMPTS")
@Getter
@SequenceGenerator(
        name = "LOGIN_ATTEMPTS_SEQ",
        sequenceName = "LOGIN_ATTEMPTS_SEQ",
        allocationSize = 1
)
@AllArgsConstructor
@NoArgsConstructor
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOGIN_ATTEMPTS_SEQ")

    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false, unique = true)
    private Member member;

    private int failedCount;

    private boolean accountNonLocked;

    private LocalDateTime lockTime;

    // 생성 시 기본값 세팅용 생성자
    public LoginAttempt(Member member) {
        this.member = member;
        this.failedCount = 0;
        this.accountNonLocked = true;
        this.lockTime = null;
    }

    public void increaseFailedCount(int maxAttempt) {
        this.failedCount++;
        if (this.failedCount >= maxAttempt) {
            this.accountNonLocked = false;
            this.lockTime = LocalDateTime.now();
        }
    }

    public void reset() {
        this.failedCount = 0;
        this.accountNonLocked = true;
        this.lockTime = null;
    }

    public boolean isLocked() {
        return !this.accountNonLocked;
    }

}
