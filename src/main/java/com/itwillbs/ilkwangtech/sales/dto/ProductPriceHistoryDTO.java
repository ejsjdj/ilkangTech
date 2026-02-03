package com.itwillbs.ilkwangtech.sales.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductPriceHistoryDTO {
    private Long priceHistoryId;
    private Long itemStockId;
    private String productName;
    private Long customerId;
    private String customerName;
    private BigDecimal unitPrice;
    private BigDecimal previousPrice;
    private LocalDateTime effectiveDate;
    private LocalDateTime endDate;
    private String changeReason;
    private String registeredBy;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getPriceHistoryId() { return priceHistoryId; }
    public void setPriceHistoryId(Long priceHistoryId) { this.priceHistoryId = priceHistoryId; }

    public Long getItemStockId() { return itemStockId; }
    public void setItemStockId(Long itemStockId) { this.itemStockId = itemStockId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getPreviousPrice() { return previousPrice; }
    public void setPreviousPrice(BigDecimal previousPrice) { this.previousPrice = previousPrice; }

    public LocalDateTime getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDateTime effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public String getRegisteredBy() { return registeredBy; }
    public void setRegisteredBy(String registeredBy) { this.registeredBy = registeredBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
