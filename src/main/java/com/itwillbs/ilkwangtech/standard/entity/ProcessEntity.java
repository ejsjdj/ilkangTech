package com.itwillbs.ilkwangtech.standard.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// 공정정보 엔티티
@Entity
@Table(name = "operation_info")
@Getter
@Setter
@NoArgsConstructor
public class ProcessEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Builder
    public ProcessEntity(String operationId, String name, String description, Member member, LocalDate createdAt){
        this.operationId = operationId;
        this.name = name;
        this.description = description;
        this.member = member;
        this.createdAt = createdAt;
    }

}
