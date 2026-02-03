package com.itwillbs.ilkwangtech.sales.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CustomerDTO {

    private Long customerId;
    private String customerName;
    private String businessNumber;
    private String representativeName;
    private String phoneNumber;
    private String faxNumber;
    private String address;
    private String email;
    private String contactPerson;
    private String remark;
    
    // 추가 필드들 (엔티티에는 없지만 UI에서 필요한 필드)
    private String businessType;
    private String status;
    private String managerPhone;
    private String managerEmail;
    private String website;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // 통계 필드들
    private Integer totalOrders;
    private Long totalAmount;
    private Long avgOrderAmount;

}
