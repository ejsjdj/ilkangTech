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
@Table(name = "RAW_MATERIAL")
@Getter @Setter
public class RawMaterial {
    @Id
    @Column(name = "rm_id", length = 30)
    private String rmId; // 원자재 LOT 번호

    @Column(name = "material_name", length = 50)
    private String materialName; // 원자재명 

    @Column(name = "supplier_id", length = 30)
    private String supplierId; // 공급업체 코드

    @Column(name = "received_date")
    private LocalDateTime receivedDate; // 입고일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotMaster lotMaster; // 연결된 마스터 LOT
}
