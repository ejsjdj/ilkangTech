package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.ItemStockDTO;
import com.itwillbs.ilkwangtech.sales.dto.WareHouseDTO;
import com.itwillbs.ilkwangtech.sales.dto.WarehouseTransactionDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface WareHouseService {
    List<WareHouseDTO> getAllWarehouses();
    WareHouseDTO getWarehouseById(Long id);
    WareHouseDTO createWarehouse(WareHouseDTO warehouseDTO);
    WareHouseDTO updateWarehouse(Long id, WareHouseDTO warehouseDTO);
    void deleteWarehouse(Long id);
    
    // 재고 관리
    List<ItemStockDTO> getAllItemStocks();
    List<ItemStockDTO> getItemStocksByWarehouse(Long warehouseId);
    ItemStockDTO getItemStockById(Long id);
    ItemStockDTO addItemStock(ItemStockDTO itemStockDTO);
    ItemStockDTO updateItemStock(Long id, ItemStockDTO itemStockDTO);
    void deleteItemStock(Long id);
    
    // 입출고 기능
    ItemStockDTO stockIn(Long itemStockId, Integer quantity, String referenceNumber, String operator, String description);
    ItemStockDTO stockOut(Long itemStockId, Integer quantity, String referenceNumber, String operator, String description);
    boolean checkStockAvailability(Long itemStockId, Integer requiredQuantity);
    
    // 입출고 기록
    List<WarehouseTransactionDTO> getAllTransactions();
    List<WarehouseTransactionDTO> getTransactionsByWarehouse(Long warehouseId);
    List<WarehouseTransactionDTO> getTransactionsByItemStock(Long itemStockId);
    List<WarehouseTransactionDTO> getTransactionsByDateRange(Long warehouseId, LocalDateTime startDate, LocalDateTime endDate);
    List<WarehouseTransactionDTO> getTransactionsByProduct(String productName);
    List<WarehouseTransactionDTO> getTransactionsByReference(String referenceNumber);
}
