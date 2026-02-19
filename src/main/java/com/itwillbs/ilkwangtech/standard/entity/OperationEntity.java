package com.itwillbs.ilkwangtech.standard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "operation_info")
@Getter
@Setter

public class OperationEntity {

    @Id
    @Column(name = "id", length = 10)
    private Long id;

    @Column(name = "operation_id")
    private String operationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private MachineEntity machine;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "description", length = 10)
    private String description;

}
