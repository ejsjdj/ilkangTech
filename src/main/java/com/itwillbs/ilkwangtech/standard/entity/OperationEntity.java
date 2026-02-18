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
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private MachineEntity machine; // 설비 정보와 연결

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "description", length = 10)
    private String description;

}
