package com.itwillbs.ilkwangtech.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;

@Entity
@Table(name = "banks")
@ToString
public class Banks {

    // 은행 (4 국민은행 20 우리은행 88 신한은행 81 하나은행 11 농협은행 23 SC제일은행 27 시티은행)
    //     (90 카카오뱅크 92 토스뱅크)
    //     (48 신용협동조합 45 새마을금고 2 산업은행)
    @Id
    private int id;

    @Column(unique = true)
    private String bankCode;

    @Column(unique = true)
    private String bankName;

    private String englishName;
    private String category;
    private boolean isActive;
}
