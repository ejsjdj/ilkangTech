package com.itwillbs.ilkwangtech.standard.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "operation_route_info")
public class ProcessRouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id", length = 10)
    private String routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private ProcessEntity operation;

    @Column(name = "item_id", length = 10)
    private Long itemId;

    @Column(name = "sequence")
    private Long sequence;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ProcessRouteEntity(Long id,
                              String routeId,
                              ProcessEntity operation,
                              Long itemId,
                              Long sequence,
                              String routeName,
                              String description,
                              String note,
                              LocalDate createdAt,
                              Member member){
        this.id = id;
        this.routeId = routeId;
        this.operation = operation;
        this.itemId = itemId;
        this.sequence = sequence;
        this.routeName = routeName;
        this.description = description;
        this.note = note;
        this.createdAt = createdAt;
        this.member = member;
    }

}
