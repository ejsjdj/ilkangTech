package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.equipment.entity.Equipment;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "production_worker")
public class ProductionWorkerEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instruct_id")
    private ProductionInstructEntity header;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private ProcessEntity process;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "status")
    private String status;

    @Column(name = "lot_id")
    private String lot;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "production_qty")
    private Long productionQty;

    @Column(name = "addition_qty")
    private Long additionQty;

    @Column(name = "defective_qty")
    private Long defectiveQty;

    @Column(name = "actual_qty")
    private Long actualQty;

    @Column(name = "sequence")
    private Long sequence;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @OneToOne
    @JoinColumn(name = "eq_id")
    private Equipment equipment;

    public static ProductionWorkerEntity create(ProcessEntity process,
                                                Member member,
                                                String lot,
                                                LocalDateTime startTime,
                                                LocalDateTime endTime,
                                                Long productionQty,
                                                Long additionQty,
                                                ItemEntity item,
                                                Long sequence){

        ProductionWorkerEntity line = new ProductionWorkerEntity();

        line.process = process;
        line.member = member;
        line.status = "READY";
        line.lot = lot;
        line.startTime = startTime;
        line.endTime = endTime;
        line.productionQty = productionQty;
        line.additionQty = additionQty;
        line.item = item;
        line.sequence = sequence;

        return line;
    }
}
