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
@Table(name = "EQUIPMENT_FAILURE")
@Getter 
@Setter
@ToString
@NoArgsConstructor
public class EqFailure {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAILURE_ID")
    private Long failureId;

    @Column(name = "EQUIP_CODE", nullable = false)
    private String equipCode;

    @Column(name = "PROC_CODE", nullable = false)
    private String procCode;

    @Column(name = "FAILURE_DESC", nullable = false, length = 1000)
    private String failureDesc;

    @Column(name = "OCCURRED_AT", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "STATUS")
    private String status = "접수";

    @Column(name = "FIXED_AT")
    private LocalDateTime fixedAt;

    @Column(name = "REPAIR_STAFF")
    private String repairStaff;

    @Column(name = "FAILURE_LEVEL")
    private String failureLevel;

}
