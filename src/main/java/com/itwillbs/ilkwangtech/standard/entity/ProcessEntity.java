package com.itwillbs.ilkwangtech.standard.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "operation_info")
@Getter
@Setter
public class ProcessEntity {

    @Id
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "description", length = 10)
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
