package com.itwillbs.ilkwangtech.messenger.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chatMessage")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roomId", nullable = false)
    private Long roomId;

    @Column(name = "memberId", nullable = false)
    private Long memberId;

    @Column(name = "msgType", nullable = false, length = 20)
    private String msgType; // TEXT, SYSTEM, FILE

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (msgType == null) {
            msgType = "TEXT";
        }
    }

    // getter / setter
}
