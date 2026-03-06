package com.itwillbs.ilkwangtech.process.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PART_PRODUCTION")
@Getter @Setter
public class PartProduction {
    @Id
    @Column(name = "part_id", length = 30)
    private String partId; // 부품 LOT 번호

    @Column(name = "part_name", length = 50)
    private String partName; // 부품명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotMaster lotMaster; // 연결된 마스터 LOT

    @Column(name = "used_rm_id", length = 30)
    private String usedRmId; // 사용된 원자재 LOT 번호

    @Column(name = "production_date")
    private LocalDateTime productionDate; // 생산일자
}
