package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 생산지시 엔티티
@Entity
@Getter
@Table(name = "production_instruct")
public class ProductionInstructEntity {

    // 작업지시 ID
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작업지시 코드
    @Column(name = "instruct_code")
    private String instructCode;

    // LOT
    @Column(name = "lot_id")
    private Long lotId;

    // 생산계획 ID
    @ManyToOne
    @JoinColumn(name = "production_id")
    private ProductionPlaneEntity productionId;

    // 품목 ID
    @Column
    private Long item;

    // 공정 ID
    @ManyToOne
    @JoinColumn(name = "operation_id")
    private ProcessEntity process;

    // 작업자
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workers_info_id")
    private List<ProductionWorkerEntity> workers;

    // 지시 수량
    @Column(name = "instruct_qty")
    private Long instructQty;

    // 지시등록일
    @Column(name = "start_date")
    private LocalDateTime startDate;

    // 생산완료일
    @Column(name = "end_date")
    private LocalDateTime endDate;

    // 상태
    @Column(name = "status")
    private String status;

    // 불량
    @Column(name = "defective")
    private Long defective;


    public static ProductionInstructEntity saveHeader(
            String instructCode,
            Long lotId,
            ProductionPlaneEntity productionId,
            Long item,
            ProcessEntity process,
            Long instructQty,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String status,
            Long defective
    ){
        ProductionInstructEntity header = new ProductionInstructEntity();
        header.instructCode = instructCode;
        header.lotId = lotId;
        header.productionId = productionId;
        header.item = item;
        header.process = process;
        header.instructQty = instructQty;
        header.startDate = startDate;
        header.endDate = endDate;
        header.status = status;
        header.defective = defective;

        return header;
    }

    // 라인 필드 저장 메서드
    public void saveLine(ProductionWorkerEntity worker) {
        this.workers.add(worker);
        worker.setHeader(this);
    }
}
