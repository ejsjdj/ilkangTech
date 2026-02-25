package com.itwillbs.ilkwangtech.quality.entity;

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
@Table(name = "QC_ITEM")
@Getter 
@Setter
@ToString
@NoArgsConstructor
public class QcItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qc_item_id")
    private Long qcItemId;

	// 공정명
    @Column(name = "proc_code", nullable = false, length = 20)
    private String procCode;

    // 품질관리명
    @Column(name = "qc_name", nullable = false, length = 100)
    private String qcName;

    // 세부내용
    @Column(name = "content")
    private String content;
	
}
