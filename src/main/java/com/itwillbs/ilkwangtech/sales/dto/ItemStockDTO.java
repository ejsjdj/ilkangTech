package com.itwillbs.ilkwangtech.sales.dto;

public class ItemStockDTO {
    private Long itemStockId;
    private Long warehouseId;
    private String warehouseName;
    private String productName;
    private String productCode;
    private Integer quantity;
    private String unit;
    private String description;

    // 현재 기본 단가
    private java.math.BigDecimal currentUnitPrice;

    // Getters and Setters
    public Long getItemStockId() { return itemStockId; }
    public void setItemStockId(Long itemStockId) { this.itemStockId = itemStockId; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public java.math.BigDecimal getCurrentUnitPrice() { return currentUnitPrice; }
    public void setCurrentUnitPrice(java.math.BigDecimal currentUnitPrice) { this.currentUnitPrice = currentUnitPrice; }
}
