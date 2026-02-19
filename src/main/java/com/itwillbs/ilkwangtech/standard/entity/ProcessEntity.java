package com.itwillbs.ilkwangtech.standard.entity;

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
    private String routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private OperationEntity operation;

    @Column(name = "item_id", length = 10)
    private String itemId;

    @Column(name = "sequence")
    private Integer sequence;

}
