package com.itwillbs.ilkwangtech.process.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "LOT_MASTER")
@Getter 
@Setter
public class LotMaster {
    @Id
    @Column(name = "lot_id", length = 30)
    private String lotId; // LOT 고유 번호

    @Column(name = "lot_type", length = 10)
    private String lotType; // RW, ST, IN, SAM 등

    @Column(name = "parent_lot_id", length = 30)
    private String parentLotId; // 상위 LOT 번호 (역추적용)

    @Column(name = "product_id", length = 30)
    private String productId; // 제품/부품 코드

    private Integer quantity; // 해당 LOT 수량

    @Column(name = "created_date")
    private LocalDateTime createdDate; // 생성일시

    @Column(length = 10)
    private String status; // 진행중, 완료, 불합격 등
}
