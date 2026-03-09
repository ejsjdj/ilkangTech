package com.itwillbs.ilkwangtech.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailDTO {
    private CompanyDTO company;
    @Builder.Default
    private List<OrderDTO> salesHistory = new java.util.ArrayList<>();
    @Builder.Default
    private List<PurchaseOrderDTO> purchaseHistory = new java.util.ArrayList<>();
    @Builder.Default
    private Map<String, Long> monthlySales = new java.util.TreeMap<>();
    @Builder.Default
    private Map<String, Long> monthlyPurchases = new java.util.TreeMap<>();
    @Builder.Default
    private java.util.Set<String> allMonths = new java.util.TreeSet<>();
}
