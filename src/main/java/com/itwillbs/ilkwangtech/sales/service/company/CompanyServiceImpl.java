package com.itwillbs.ilkwangtech.sales.service.company;

import com.itwillbs.ilkwangtech.common.annotation.Audit;
import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.constant.CompanyStatus;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDetailDTO;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseCompanyDTO;
import com.itwillbs.ilkwangtech.sales.repository.CompanyRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final JdbcTemplate jdbcTemplate;
    private final com.itwillbs.ilkwangtech.sales.mapper.CompanyMapper companyMapper;
    private final com.itwillbs.ilkwangtech.sales.mapper.OrderMapper orderMapper;
    private final com.itwillbs.ilkwangtech.sales.repository.PurchaseOrderHeaderRepository purchaseOrderHeaderRepository;
    private final com.itwillbs.ilkwangtech.inventorymg.repository.InventoryRepository inventoryRepository;

    @PostConstruct
    public void initTable() {
        log.info("Checking COMPANY table schema...");
        try {
            // Oracle은 기본적으로 대문자로 테이블/컬럼명을 관리함
            checkAndAddColumn("COMPANY", "BUSINESS_NUMBER", "VARCHAR2(20)");
            checkAndAddColumn("COMPANY", "FAX_NO", "VARCHAR2(20)");
            checkAndAddColumn("COMPANY", "EMAIL", "VARCHAR2(100)");
            checkAndAddColumn("COMPANY", "ADDRESS", "VARCHAR2(255)");
            checkAndAddColumn("COMPANY", "MANAGER_NAME", "VARCHAR2(50)");
            checkAndAddColumn("COMPANY", "MANAGER_TEL", "VARCHAR2(20)");
            checkAndAddColumn("COMPANY", "STATUS", "INTEGER DEFAULT 1");
        } catch (Exception e) {
            log.error("Failed to update COMPANY table schema: {}", e.getMessage());
        }
    }

    private void checkAndAddColumn(String tableName, String columnName, String columnDef) {
        String checkSql = "SELECT count(*) FROM user_tab_columns WHERE table_name = ? AND column_name = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, tableName.toUpperCase(), columnName.toUpperCase());

        if (count == null || count == 0) {
            log.info("Adding column {} to table {}...", columnName, tableName);
            String alterSql = String.format("ALTER TABLE %s ADD (%s %s)", tableName, columnName, columnDef);
            jdbcTemplate.execute(alterSql);
            log.info("Column {} added successfully.", columnName);
        }
    }

    @Override
    @Transactional
    @Audit(action = "거래처 등록", entity = "Company")
    public void createCompany(CompanyDTO companyDTO) {
        if (companyRepository.findByName(companyDTO.getCompanyName()) != null)
            throw new IllegalArgumentException("이미 등록된 회사입니다.");
        if (companyDTO.getStatus() == null) {
            companyDTO.setStatus(CompanyStatus.ACTIVE);
        }
        companyRepository.save(companyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompanyList(Pageable pageable, CompanyCategory companyType) {
        long offset = pageable.getOffset();
        long pageSize = pageable.getPageSize();
        return companyRepository.selectByPage(offset, pageSize, companyType);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCount(CompanyCategory companyType) {
        return companyRepository.selectCount(companyType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseCompanyDTO> selectPurchaseCompany() {
        return companyRepository.selectCompanyType3();
    }

    @Override
    public CompanyDTO getCompany(Long id) {
        return companyMapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDetailDTO getCompanyDetail(Long id) {
        CompanyDTO company = getCompany(id);
        if (company == null) return null;

        // 통계를 위해 전체 내역을 가져옴 (페이징 없이)
        List<com.itwillbs.ilkwangtech.sales.dto.OrderDTO> allSales = orderMapper.selectByCompanyId(id);
        Map<String, Long> monthlySales = new TreeMap<>();

        if (allSales != null) {
            for (com.itwillbs.ilkwangtech.sales.dto.OrderDTO order : allSales) {
                // 각 주문의 상세 합계 계산 (데이터 정합성 보장)
                long total = 0;
                List<com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO> details = orderMapper.selectDetailsByOrderId(order.getOrderId());
                if (details != null) {
                    for (com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO d : details) {
                        total += (d.getQuantity() != null ? d.getQuantity() : 0) * (d.getUnitPrice() != null ? d.getUnitPrice() : 0L);
                    }
                }
                order.setTotalAmount(total);

                if (order.getOrderDate() != null) {
                    String month = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    monthlySales.put(month, monthlySales.getOrDefault(month, 0L) + total);
                }
            }
        }

        List<com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity> allPurchases = purchaseOrderHeaderRepository.findByCompanyId(id);
        Map<String, Long> monthlyPurchases = new TreeMap<>();
        if (allPurchases != null) {
            for (com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity h : allPurchases) {
                Long amt = h.getAmount() != null ? h.getAmount() : 0L;
                if (h.getOrderDate() != null) {
                    String month = h.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    monthlyPurchases.put(month, monthlyPurchases.getOrDefault(month, 0L) + amt);
                }
            }
        }

        java.util.Set<String> allMonths = new java.util.TreeSet<>();
        allMonths.addAll(monthlySales.keySet());
        allMonths.addAll(monthlyPurchases.keySet());

        List<com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO> purchaseDTOs = new ArrayList<>();
        if (allPurchases != null) {
            for (com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity h : allPurchases) {
                purchaseDTOs.add(com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO.builder()
                        .id(h.getId())
                        .amount(h.getAmount())
                        .build());
            }
        }

        return CompanyDetailDTO.builder()
                .company(company)
                .salesHistory(allSales != null ? allSales : new ArrayList<>())
                .purchaseHistory(purchaseDTOs)
                .monthlySales(monthlySales)
                .monthlyPurchases(monthlyPurchases)
                .allMonths(allMonths)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<com.itwillbs.ilkwangtech.sales.dto.OrderDTO> getSalesHistory(Long companyId, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<com.itwillbs.ilkwangtech.sales.dto.OrderDTO> list = orderMapper.selectByCompanyIdPage(companyId, offset, pageSize);
        long total = orderMapper.countByCompanyId(companyId);

        if (list != null) {
            for (com.itwillbs.ilkwangtech.sales.dto.OrderDTO order : list) {
                processOrderStockAndAmount(order);
            }
        }
        return new org.springframework.data.domain.PageImpl<>(list != null ? list : new ArrayList<>(), pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO> getPurchaseHistory(Long companyId, Pageable pageable) {
        org.springframework.data.domain.Page<com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity> page = purchaseOrderHeaderRepository.findByCompanyCompanyId(companyId, pageable);

        List<com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO> dtoList = page.getContent().stream()
                .map(h -> com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDTO.builder()
                        .id(h.getId())
                        .purchaseOrderCode(h.getPurchaseOrderCode())
                        .company(h.getCompany() != null ? h.getCompany().getCompanyName() : "")
                        .status(h.getStatus())
                        .orderDate(h.getOrderDate() != null ? h.getOrderDate().toString() : "")
                        .amount(h.getAmount() != null ? h.getAmount() : 0L)
                        .build())
                .collect(Collectors.toList());

        return new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    private void processOrderStockAndAmount(com.itwillbs.ilkwangtech.sales.dto.OrderDTO order) {
        if (order.getOrderId() == null) return;
        List<com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO> details = orderMapper.selectDetailsByOrderId(order.getOrderId());
        order.setOrderDetails(details);

        if (order.getOrderStatus() != null &&
                order.getOrderStatus() != com.itwillbs.ilkwangtech.sales.constant.OrderStatus.COMPLETED) {

            boolean allReady = true;
            if (details != null && !details.isEmpty()) {
                for (com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO d : details) {
                    if (d.getItemId() == null) { allReady = false; break; }
                    Long currentStock = inventoryRepository.getTotalQuantityByItemId(d.getItemId());
                    if (currentStock == null || currentStock < (d.getQuantity() != null ? d.getQuantity() : 0)) {
                        allReady = false;
                        break;
                    }
                }
            } else {
                allReady = false;
            }
            order.setOrderStatus(allReady ? com.itwillbs.ilkwangtech.sales.constant.OrderStatus.READY : com.itwillbs.ilkwangtech.sales.constant.OrderStatus.NEED_PRODUCTION);
        }

        long total = 0;
        if (details != null) {
            for (com.itwillbs.ilkwangtech.sales.dto.OrderDetailDTO d : details) {
                total += (d.getQuantity() != null ? d.getQuantity() : 0) * (d.getUnitPrice() != null ? d.getUnitPrice() : 0L);
            }
        }
        order.setTotalAmount(total);

        if (order.getOrderStatus() != null) {
            order.setOrderStatusDescription(order.getOrderStatus().getDescription());
        }
    }
}