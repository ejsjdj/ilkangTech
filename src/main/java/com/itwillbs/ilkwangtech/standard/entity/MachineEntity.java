package com.itwillbs.ilkwangtech.standard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "machine_info")
@Getter
@Setter
public class MachineEntity {

    @Id
    @Column(name = "id", length = 10)
    private String id;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "manufacturer", length = 10)
    private String manufacturer;

    @Column(name = "electric_power", length = 10)
    private String electricPower;

}
