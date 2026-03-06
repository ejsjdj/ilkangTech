package com.itwillbs.ilkwangtech.equipment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "EQ_HISTORY")
@Getter 
@Setter
@ToString
@NoArgsConstructor
public class EqHistory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long eqHistoryId;

    @Column(name = "eq_name", nullable = false, length = 100)
    private String eqName;

    @Column(name = "work_order_no", nullable = false, length = 50)
    private String workOrderNo;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "operator_name", nullable = false, length = 50)
    private String operatorName;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private String duration;
}

