package com.itwillbs.ilkwangtech.sales.dto;

import java.time.LocalDateTime;

public class WarehouseTransactionDTO {
    private Long transactionId;
    private Long warehouseId;
    private String warehouseName;
    private Long itemStockId;
    private String productName;
    private String transactionType;
    private Integer quantity;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private LocalDateTime transactionDate;
    private String referenceNumber;
    private String description;
    private String operator;

    // Getters and Setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public Long getItemStockId() { return itemStockId; }
    public void setItemStockId(Long itemStockId) { this.itemStockId = itemStockId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getBeforeQuantity() { return beforeQuantity; }
    public void setBeforeQuantity(Integer beforeQuantity) { this.beforeQuantity = beforeQuantity; }

    public Integer getAfterQuantity() { return afterQuantity; }
    public void setAfterQuantity(Integer afterQuantity) { this.afterQuantity = afterQuantity; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
}
