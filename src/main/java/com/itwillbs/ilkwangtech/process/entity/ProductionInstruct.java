package com.itwillbs.ilkwangtech.process.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "production_instruct") // 실제 DB 테이블명 매핑
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionInstruct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID 컬럼

    @Column(name = "instruct_code", nullable = false)
    private String instructCode; // INSTRUCT_CODE

    @Column(name = "item_id")
    private Long itemId; // ITEM_ID

    @Column(name = "instruct_qty")
    private Integer instructQty; // INSTRUCT_QTY (계획수량)

    @Column(name = "defective")
    private Integer defective; // DEFECTIVE (불량수량)

    @Column(name = "status")
    private String status; // STATUS (WAITING, PROGRESS, COMPLETE 등)

    @Column(name = "operation_id")
    private Long operationId; // OPERATION_ID (공정 테이블 조인용 FK)

    @Column(name = "start_date")
    private LocalDateTime startDate; // START_DATE (시작일시)

    @Column(name = "end_date")
    private LocalDateTime endDate; // END_DATE (종료/목표일시)
    
    // 비즈니스 로직: 실시간 양품 수량 계산
    public Integer calculateProductionQty() {
        return (this.instructQty != null ? this.instructQty : 0) - (this.defective != null ? this.defective : 0);
    }
}