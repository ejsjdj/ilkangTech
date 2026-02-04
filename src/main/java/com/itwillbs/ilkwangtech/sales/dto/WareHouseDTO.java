package com.itwillbs.ilkwangtech.sales.dto;

import java.util.List;

public class WareHouseDTO {
    private Long warehouseId;
    private String warehouseName;
    private List<ItemStockDTO> itemStocks;

    // Getters and Setters
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public List<ItemStockDTO> getItemStocks() { return itemStocks; }
    public void setItemStocks(List<ItemStockDTO> itemStocks) { this.itemStocks = itemStocks; }
}
