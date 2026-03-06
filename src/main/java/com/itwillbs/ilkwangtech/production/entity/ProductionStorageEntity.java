package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "production_storage")
public class ProductionStorageEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instruct_id")
    private ProductionInstructEntity instruct;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "production_qty")
    private Long productionQty;

    @Column(name = "storage_date")
    private LocalDateTime storage_date;

}
