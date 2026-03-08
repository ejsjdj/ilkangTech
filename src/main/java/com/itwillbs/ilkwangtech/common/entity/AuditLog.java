package com.itwillbs.ilkwangtech.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeNumber; // 작업을 수행한 사용자 사번

    @Column(nullable = false)
    private String memberName; // 사용자 이름

    @Column(nullable = false)
    private String action; // 수행 작업 (예: CREATE_ORDER, UPDATE_STOCK)

    @Column(length = 255)
    private String entityName; // 대상 엔티티 (예: Order, Inventory)

    @Column
    private String entityId; // 대상 엔티티의 ID

    @Lob
    @Column(length = 4000)
    private String details; // 상세 내용 (JSON 또는 설명)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String ipAddress; // 요청 IP
}
