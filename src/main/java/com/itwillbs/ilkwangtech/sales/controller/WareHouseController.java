package com.itwillbs.ilkwangtech.sales.controller;

import com.itwillbs.ilkwangtech.sales.dto.WareHouseDTO;
import com.itwillbs.ilkwangtech.sales.dto.ItemStockDTO;
import com.itwillbs.ilkwangtech.sales.dto.WarehouseTransactionDTO;
import com.itwillbs.ilkwangtech.sales.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sales/warehouse")
@RequiredArgsConstructor
/**
 * 창고를 관리하는 컨트롤러
 * 어떤창고에 어떤 제품이 얼마나 보관되어 있는지 확인한다.
 * 제품생산이 완료되어서 물건을 받으면 창고에 해당 제품의 재고를 추가한다.
 * 출하가 되면 해당 제품의 재고를 감소시킨다
 * 제품이 들어오고 제품이 나가고를 기록한다.
 */
public class WareHouseController {

    private final WareHouseService warehouseService;

    // 창고 관리
    @GetMapping
    public ResponseEntity<List<WareHouseDTO>> getAllWarehouses() {
        List<WareHouseDTO> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WareHouseDTO> getWarehouseById(@PathVariable Long id) {
        WareHouseDTO warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(warehouse);
    }

    @PostMapping
    public ResponseEntity<WareHouseDTO> createWarehouse(@RequestBody WareHouseDTO warehouseDTO) {
        WareHouseDTO createdWarehouse = warehouseService.createWarehouse(warehouseDTO);
        return ResponseEntity.ok(createdWarehouse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WareHouseDTO> updateWarehouse(@PathVariable Long id, @RequestBody WareHouseDTO warehouseDTO) {
        WareHouseDTO updatedWarehouse = warehouseService.updateWarehouse(id, warehouseDTO);
        return ResponseEntity.ok(updatedWarehouse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok().build();
    }

    // 재고 관리
    @GetMapping("/inventory")
    public ResponseEntity<List<ItemStockDTO>> getAllItemStocks() {
        List<ItemStockDTO> itemStocks = warehouseService.getAllItemStocks();
        return ResponseEntity.ok(itemStocks);
    }

    @GetMapping("/inventory/warehouse/{warehouseId}")
    public ResponseEntity<List<ItemStockDTO>> getItemStocksByWarehouse(@PathVariable Long warehouseId) {
        List<ItemStockDTO> itemStocks = warehouseService.getItemStocksByWarehouse(warehouseId);
        return ResponseEntity.ok(itemStocks);
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<ItemStockDTO> getItemStockById(@PathVariable Long id) {
        ItemStockDTO itemStock = warehouseService.getItemStockById(id);
        return ResponseEntity.ok(itemStock);
    }

    @PostMapping("/inventory")
    public ResponseEntity<ItemStockDTO> addItemStock(@RequestBody ItemStockDTO itemStockDTO) {
        ItemStockDTO createdItemStock = warehouseService.addItemStock(itemStockDTO);
        return ResponseEntity.ok(createdItemStock);
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<ItemStockDTO> updateItemStock(@PathVariable Long id, @RequestBody ItemStockDTO itemStockDTO) {
        ItemStockDTO updatedItemStock = warehouseService.updateItemStock(id, itemStockDTO);
        return ResponseEntity.ok(updatedItemStock);
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<Void> deleteItemStock(@PathVariable Long id) {
        warehouseService.deleteItemStock(id);
        return ResponseEntity.ok().build();
    }

    // 입출고 기능
    @PostMapping("/inventory/{itemStockId}/stock-in")
    public ResponseEntity<ItemStockDTO> stockIn(@PathVariable Long itemStockId, 
                                              @RequestParam Integer quantity,
                                              @RequestParam(required = false) String referenceNumber,
                                              @RequestParam(required = false) String operator,
                                              @RequestParam(required = false) String description) {
        ItemStockDTO itemStock = warehouseService.stockIn(itemStockId, quantity, referenceNumber, operator, description);
        return ResponseEntity.ok(itemStock);
    }

    @PostMapping("/inventory/{itemStockId}/stock-out")
    public ResponseEntity<ItemStockDTO> stockOut(@PathVariable Long itemStockId, 
                                               @RequestParam Integer quantity,
                                               @RequestParam(required = false) String referenceNumber,
                                               @RequestParam(required = false) String operator,
                                               @RequestParam(required = false) String description) {
        ItemStockDTO itemStock = warehouseService.stockOut(itemStockId, quantity, referenceNumber, operator, description);
        return ResponseEntity.ok(itemStock);
    }

    @GetMapping("/inventory/{itemStockId}/check-availability")
    public ResponseEntity<Boolean> checkStockAvailability(@PathVariable Long itemStockId, 
                                                        @RequestParam Integer requiredQuantity) {
        boolean available = warehouseService.checkStockAvailability(itemStockId, requiredQuantity);
        return ResponseEntity.ok(available);
    }

    // 입출고 기록 조회
    @GetMapping("/transactions")
    public ResponseEntity<List<WarehouseTransactionDTO>> getAllTransactions() {
        List<WarehouseTransactionDTO> transactions = warehouseService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/warehouse/{warehouseId}")
    public ResponseEntity<List<WarehouseTransactionDTO>> getTransactionsByWarehouse(@PathVariable Long warehouseId) {
        List<WarehouseTransactionDTO> transactions = warehouseService.getTransactionsByWarehouse(warehouseId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/item/{itemStockId}")
    public ResponseEntity<List<WarehouseTransactionDTO>> getTransactionsByItemStock(@PathVariable Long itemStockId) {
        List<WarehouseTransactionDTO> transactions = warehouseService.getTransactionsByItemStock(itemStockId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/warehouse/{warehouseId}/date-range")
    public ResponseEntity<List<WarehouseTransactionDTO>> getTransactionsByDateRange(
            @PathVariable Long warehouseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<WarehouseTransactionDTO> transactions = warehouseService.getTransactionsByDateRange(warehouseId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/product")
    public ResponseEntity<List<WarehouseTransactionDTO>> getTransactionsByProduct(@RequestParam String productName) {
        List<WarehouseTransactionDTO> transactions = warehouseService.getTransactionsByProduct(productName);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/reference")
    public ResponseEntity<List<WarehouseTransactionDTO>> getTransactionsByReference(@RequestParam String referenceNumber) {
        List<WarehouseTransactionDTO> transactions = warehouseService.getTransactionsByReference(referenceNumber);
        return ResponseEntity.ok(transactions);
    }

}
