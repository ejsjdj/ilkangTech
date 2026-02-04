package com.itwillbs.ilkwangtech.sales.dto;

import java.math.BigDecimal;

public class PriceNegotiationDTO {
    private Long itemStockId;
    private String productName;
    private Long customerId;
    private String customerName;
    private BigDecimal currentPrice;
    private BigDecimal newPrice;
    private String changeReason;
    private String salesPerson;
    private String notes;

    // Getters and Setters
    public Long getItemStockId() { return itemStockId; }
    public void setItemStockId(Long itemStockId) { this.itemStockId = itemStockId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public BigDecimal getNewPrice() { return newPrice; }
    public void setNewPrice(BigDecimal newPrice) { this.newPrice = newPrice; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public String getSalesPerson() { return salesPerson; }
    public void setSalesPerson(String salesPerson) { this.salesPerson = salesPerson; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
