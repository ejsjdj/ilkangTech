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
@Table(name = "QUALITY_CHECK")
@Getter @Setter
public class QualityCheck {
    @Id
    @Column(name = "qc_id", length = 30)
    private String qcId; // 품질검사 LOT 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotMaster lotMaster; // 연결된 마스터 LOT

    @Column(length = 10)
    private String result; // 합격/불합격

    @Column(name = "inspector_id", length = 30)
    private String inspectorId; // 검사자 ID

    @Column(name = "qc_date")
    private LocalDateTime qcDate; // 검사일자
}
