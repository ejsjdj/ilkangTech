package com.itwillbs.ilkwangtech.equipment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Equipment {
	
	@Id
	@Column(name = "id")
    private Long id;	// PK (1, 2, 3...)
	
    @Column(name = "equip_code", length = 20, nullable = false, unique = true)
    private String equipCode; // 설비 코드 

    @Column(name = "equip_name", nullable = false, length = 100)
    private String equipName; // 설비 명

    @Column(name = "proc_code", nullable = false, length = 10)
    private String procCode; // 공정 코드

    @Column(name = "maker", length = 50)
    private String maker; // 제조사

    @Column(name = "price")
    private Long price; // 설비 가격 

    @Column(name = "inst_date")
    private LocalDate instDate; // 설치일

    @Column(name = "status", length = 20)
    private String status; // 사용 여부 (가동/점검 등)

    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate = LocalDateTime.now(); // 등록 일시
    
    @Column(name = "worker_id")
    private Long workerId;

    @Column(name = "last_work_date")
    private LocalDateTime lastWorkDate;
}
