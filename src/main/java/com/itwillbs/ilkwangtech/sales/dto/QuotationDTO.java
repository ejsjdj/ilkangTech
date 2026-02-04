package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.QuotationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuotationDTO {
    private Long quotationId;
    private String quotationNo;
    private Long customerId;
    private String customerName;
    private LocalDate quotationDate;
    private QuotationStatus status;
    private LocalDateTime leadTime;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String currency;
    private String remarks;
    private String salesPerson;
    private List<QuotationDetailDTO> details;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QuotationDetailDTO {
        private Long quotationDetailId;
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private String remarks;
    }
}
