package com.itwillbs.ilkwangtech.standard.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "operation_route_info")
public class ProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id", length = 10)
    private Long routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private OperationEntity operation;

    @Column(name = "item_id", length = 10)
    private Long itemId;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private String createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
