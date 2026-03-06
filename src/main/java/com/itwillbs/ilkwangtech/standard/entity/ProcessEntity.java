package com.itwillbs.ilkwangtech.standard.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.standard.constant.ProcessStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String operationCode;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProcessStatus status;

    @Builder
    public ProcessEntity(String operationCode, String name, String description, Member member, LocalDate createdAt, ProcessStatus status){
        this.operationCode = operationCode;
        this.name = name;
        this.description = description;
        this.member = member;
        this.createdAt = createdAt;
        this.status = status;
    }

}
