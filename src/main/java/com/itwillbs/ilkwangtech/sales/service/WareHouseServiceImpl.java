package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.ItemStockDTO;
import com.itwillbs.ilkwangtech.sales.dto.WareHouseDTO;
import com.itwillbs.ilkwangtech.sales.dto.WarehouseTransactionDTO;
import com.itwillbs.ilkwangtech.sales.entity.ItemStock;
import com.itwillbs.ilkwangtech.sales.entity.WareHouse;
import com.itwillbs.ilkwangtech.sales.entity.WarehouseTransaction;
import com.itwillbs.ilkwangtech.sales.repository.ItemStockRepository;
import com.itwillbs.ilkwangtech.sales.repository.WareHouseRepository;
import com.itwillbs.ilkwangtech.sales.repository.WarehouseTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {

    private final WareHouseRepository warehouseRepository;
    private final ItemStockRepository itemStockRepository;
    private final WarehouseTransactionRepository transactionRepository;

    // 창고 관리
    @Override
    @Transactional(readOnly = true)
    public List<WareHouseDTO> getAllWarehouses() {
        List<WareHouse> warehouses = warehouseRepository.findAll();
        return warehouses.stream()
                .map(this::convertToWarehouseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public WareHouseDTO getWarehouseById(Long id) {
        WareHouse warehouse = warehouseRepository.findByWarehouseId(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        return convertToWarehouseDTO(warehouse);
    }

    @Override
    @Transactional
    public WareHouseDTO createWarehouse(WareHouseDTO warehouseDTO) {
        WareHouse warehouse = convertToWarehouseEntity(warehouseDTO);
        WareHouse savedWarehouse = warehouseRepository.save(warehouse);
        return convertToWarehouseDTO(savedWarehouse);
    }

    @Override
    @Transactional
    public WareHouseDTO updateWarehouse(Long id, WareHouseDTO warehouseDTO) {
        WareHouse existingWarehouse = warehouseRepository.findByWarehouseId(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        
        existingWarehouse.setWarehouseName(warehouseDTO.getWarehouseName());
        WareHouse updatedWarehouse = warehouseRepository.save(existingWarehouse);
        return convertToWarehouseDTO(updatedWarehouse);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        WareHouse warehouse = warehouseRepository.findByWarehouseId(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        
        // 창고에 재고가 있으면 삭제 불가
        if (!warehouse.getItemStocks().isEmpty()) {
            throw new RuntimeException("Cannot delete warehouse with existing inventory");
        }
        
        warehouseRepository.delete(warehouse);
    }

    // 재고 관리
    @Override
    @Transactional(readOnly = true)
    public List<ItemStockDTO> getAllItemStocks() {
        List<ItemStock> itemStocks = itemStockRepository.findAll();
        return itemStocks.stream()
                .map(this::convertToItemStockDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemStockDTO> getItemStocksByWarehouse(Long warehouseId) {
        List<ItemStock> itemStocks = itemStockRepository.findByWarehouse_WarehouseId(warehouseId);
        return itemStocks.stream()
                .map(this::convertToItemStockDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemStockDTO getItemStockById(Long id) {
        ItemStock itemStock = itemStockRepository.findByItemStockId(id)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + id));
        return convertToItemStockDTO(itemStock);
    }

    @Override
    @Transactional
    public ItemStockDTO addItemStock(ItemStockDTO itemStockDTO) {
        ItemStock itemStock = convertToItemStockEntity(itemStockDTO);
        ItemStock savedItemStock = itemStockRepository.save(itemStock);
        
        // 입고 기록
        createTransaction(savedItemStock, "IN", savedItemStock.getQuantity(), 0, 
                        savedItemStock.getQuantity(), "초기 입고", "시스템");
        
        return convertToItemStockDTO(savedItemStock);
    }

    @Override
    @Transactional
    public ItemStockDTO updateItemStock(Long id, ItemStockDTO itemStockDTO) {
        ItemStock existingItemStock = itemStockRepository.findByItemStockId(id)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + id));
        
        int beforeQuantity = existingItemStock.getQuantity();
        existingItemStock.setProductName(itemStockDTO.getProductName());
        existingItemStock.setProductCode(itemStockDTO.getProductCode());
        existingItemStock.setUnit(itemStockDTO.getUnit());
        existingItemStock.setDescription(itemStockDTO.getDescription());
        
        ItemStock updatedItemStock = itemStockRepository.save(existingItemStock);
        
        // 수량 변경 시 기록
        Integer newQuantity = itemStockDTO.getQuantity();
        if (newQuantity != null && beforeQuantity != newQuantity) {
            createTransaction(updatedItemStock, "ADJUSTMENT", 
                            newQuantity - beforeQuantity,
                            beforeQuantity, newQuantity, 
                            "수량 조정", "시스템");
        }
        
        return convertToItemStockDTO(updatedItemStock);
    }

    @Override
    @Transactional
    public void deleteItemStock(Long id) {
        ItemStock itemStock = itemStockRepository.findByItemStockId(id)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + id));
        
        itemStockRepository.delete(itemStock);
    }

    // 입출고 기능
    @Override
    @Transactional
    public ItemStockDTO stockIn(Long itemStockId, Integer quantity, String referenceNumber, String operator, String description) {
        ItemStock itemStock = itemStockRepository.findByItemStockId(itemStockId)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + itemStockId));
        
        int beforeQuantity = itemStock.getQuantity();
        int afterQuantity = beforeQuantity + quantity;
        
        itemStock.setQuantity(afterQuantity);
        ItemStock savedItemStock = itemStockRepository.save(itemStock);
        
        // 입고 기록
        createTransaction(savedItemStock, "IN", quantity, beforeQuantity, afterQuantity, 
                        description, operator, referenceNumber);
        
        return convertToItemStockDTO(savedItemStock);
    }

    @Override
    @Transactional
    public ItemStockDTO stockOut(Long itemStockId, Integer quantity, String referenceNumber, String operator, String description) {
        ItemStock itemStock = itemStockRepository.findByItemStockId(itemStockId)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + itemStockId));
        
        int beforeQuantity = itemStock.getQuantity();
        if (beforeQuantity < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + beforeQuantity + ", Required: " + quantity);
        }
        
        int afterQuantity = beforeQuantity - quantity;
        itemStock.setQuantity(afterQuantity);
        ItemStock savedItemStock = itemStockRepository.save(itemStock);
        
        // 출고 기록
        createTransaction(savedItemStock, "OUT", quantity, beforeQuantity, afterQuantity, 
                        description, operator, referenceNumber);
        
        return convertToItemStockDTO(savedItemStock);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkStockAvailability(Long itemStockId, Integer requiredQuantity) {
        ItemStock itemStock = itemStockRepository.findByItemStockId(itemStockId)
                .orElseThrow(() -> new RuntimeException("ItemStock not found with id: " + itemStockId));
        
        return itemStock.getQuantity() >= requiredQuantity;
    }

    // 입출고 기록
    @Override
    @Transactional(readOnly = true)
    public List<WarehouseTransactionDTO> getAllTransactions() {
        List<WarehouseTransaction> transactions = transactionRepository.findAllOrderByDateDesc();
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseTransactionDTO> getTransactionsByWarehouse(Long warehouseId) {
        List<WarehouseTransaction> transactions = transactionRepository.findByWarehouse_WarehouseId(warehouseId);
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseTransactionDTO> getTransactionsByItemStock(Long itemStockId) {
        List<WarehouseTransaction> transactions = transactionRepository.findByItemStock_ItemStockId(itemStockId);
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseTransactionDTO> getTransactionsByDateRange(Long warehouseId, LocalDateTime startDate, LocalDateTime endDate) {
        List<WarehouseTransaction> transactions = transactionRepository.findByWarehouseAndDateBetween(warehouseId, startDate, endDate);
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseTransactionDTO> getTransactionsByProduct(String productName) {
        List<WarehouseTransaction> transactions = transactionRepository.findByProductNameContaining(productName);
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseTransactionDTO> getTransactionsByReference(String referenceNumber) {
        List<WarehouseTransaction> transactions = transactionRepository.findByReferenceNumber(referenceNumber);
        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    // 내부 메서드
    private void createTransaction(ItemStock itemStock, String transactionType, Integer quantity, 
                                 Integer beforeQuantity, Integer afterQuantity, String description, String operator) {
        createTransaction(itemStock, transactionType, quantity, beforeQuantity, afterQuantity, description, operator, null);
    }

    private void createTransaction(ItemStock itemStock, String transactionType, Integer quantity, 
                                 Integer beforeQuantity, Integer afterQuantity, String description, String operator, String referenceNumber) {
        WarehouseTransaction transaction = new WarehouseTransaction();
        transaction.setWarehouse(itemStock.getWarehouse());
        transaction.setItemStock(itemStock);
        transaction.setTransactionType(transactionType);
        transaction.setQuantity(quantity);
        transaction.setBeforeQuantity(beforeQuantity);
        transaction.setAfterQuantity(afterQuantity);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setReferenceNumber(referenceNumber);
        transaction.setDescription(description);
        transaction.setOperator(operator);
        
        transactionRepository.save(transaction);
    }

    // DTO 변환 메서드
    private WareHouseDTO convertToWarehouseDTO(WareHouse warehouse) {
        WareHouseDTO dto = new WareHouseDTO();
        dto.setWarehouseId(warehouse.getWarehouseId());
        dto.setWarehouseName(warehouse.getWarehouseName());
        dto.setItemStocks(warehouse.getItemStocks().stream()
                .map(this::convertToItemStockDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private WareHouse convertToWarehouseEntity(WareHouseDTO dto) {
        WareHouse warehouse = new WareHouse();
        warehouse.setWarehouseId(dto.getWarehouseId());
        warehouse.setWarehouseName(dto.getWarehouseName());
        return warehouse;
    }

    private ItemStockDTO convertToItemStockDTO(ItemStock itemStock) {
        ItemStockDTO dto = new ItemStockDTO();
        dto.setItemStockId(itemStock.getItemStockId());
        dto.setWarehouseId(itemStock.getWarehouse() != null ? itemStock.getWarehouse().getWarehouseId() : null);
        dto.setWarehouseName(itemStock.getWarehouse() != null ? itemStock.getWarehouse().getWarehouseName() : null);
        dto.setProductName(itemStock.getProductName());
        dto.setProductCode(itemStock.getProductCode());
        dto.setQuantity(itemStock.getQuantity());
        dto.setUnit(itemStock.getUnit());
        dto.setDescription(itemStock.getDescription());
        return dto;
    }

    private ItemStock convertToItemStockEntity(ItemStockDTO dto) {
        ItemStock itemStock = new ItemStock();
        itemStock.setItemStockId(dto.getItemStockId());
        itemStock.setProductName(dto.getProductName());
        itemStock.setProductCode(dto.getProductCode());
        itemStock.setQuantity(dto.getQuantity());
        itemStock.setUnit(dto.getUnit());
        itemStock.setDescription(dto.getDescription());
        
        if (dto.getWarehouseId() != null) {
            WareHouse warehouse = warehouseRepository.findByWarehouseId(dto.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + dto.getWarehouseId()));
            itemStock.setWarehouse(warehouse);
        }
        
        return itemStock;
    }

    private WarehouseTransactionDTO convertToTransactionDTO(WarehouseTransaction transaction) {
        WarehouseTransactionDTO dto = new WarehouseTransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setWarehouseId(transaction.getWarehouse() != null ? transaction.getWarehouse().getWarehouseId() : null);
        dto.setWarehouseName(transaction.getWarehouse() != null ? transaction.getWarehouse().getWarehouseName() : null);
        dto.setItemStockId(transaction.getItemStock() != null ? transaction.getItemStock().getItemStockId() : null);
        dto.setProductName(transaction.getItemStock() != null ? transaction.getItemStock().getProductName() : null);
        dto.setTransactionType(transaction.getTransactionType());
        dto.setQuantity(transaction.getQuantity());
        dto.setBeforeQuantity(transaction.getBeforeQuantity());
        dto.setAfterQuantity(transaction.getAfterQuantity());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setDescription(transaction.getDescription());
        dto.setOperator(transaction.getOperator());
        return dto;
    }
}
