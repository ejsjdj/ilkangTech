package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    public static ProductionWorkerEntity create(ProcessEntity process,
                                                Member member){

        ProductionWorkerEntity line = new ProductionWorkerEntity();

        line.process = process;
        line.member = member;

        return line;
    }
}
