package com.itwillbs.ilkwangtech.sales.dto;


import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderDTO {

    private String purchaseOrderCode; // 발주 번호

    private String company;// 거래처명

    private String companyManager;// 담당자명

    private String phone;// 전화번호

    private String name;// 발주 담당자

    private String status;// 발주 상태(발주접수, 발주확정, 출하완료, 검수완료, 입고완료)

    private String orderDate;// 발주 일자

    private Long amount; // 총 금액

    @Builder
    public PurchaseOrderDTO(String purchaseOrderCode,
                            String company,
                            String companyManager,
                            String phone,
                            String name,
                            String status,
                            String orderDate,
                            Long amount){

        this.purchaseOrderCode = purchaseOrderCode;
        this.company = company;
        this.companyManager = companyManager;
        this.phone = phone;
        this.name = name;
        this.status = status;
        this.orderDate = orderDate;
        this.amount = amount;
    }
}
