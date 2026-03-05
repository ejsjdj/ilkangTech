package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "production_instruct_qty")
public class ProductionInstructQtyEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private ItemEntity item;


}
