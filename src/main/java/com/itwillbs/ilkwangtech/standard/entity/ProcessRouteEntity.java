package com.itwillbs.ilkwangtech.standard.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "operation_route_info")
public class ProcessRouteEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_code")
    private String routeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private ProcessEntity operation;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

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
                              String routeCode,
                              ProcessEntity operation,
                              ItemEntity item,
                              Long sequence,
                              String routeName,
                              String description,
                              String note,
                              LocalDate createdAt,
                              Member member){
        this.id = id;
        this.routeCode = routeCode;
        this.operation = operation;
        this.item = item;
        this.sequence = sequence;
        this.routeName = routeName;
        this.description = description;
        this.note = note;
        this.createdAt = createdAt;
        this.member = member;
    }

    public void update(Long sequence, String note, Member member){
        if (sequence != null) {
            this.sequence = sequence;
        }

        if (note != null) {
            this.note = note;
        }

        this.member = member;
    }

}
