package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.constant.QuotationStatus;
import com.itwillbs.ilkwangtech.sales.dto.QuotationDTO;
import com.itwillbs.ilkwangtech.sales.entity.Customer;
import com.itwillbs.ilkwangtech.sales.entity.Quotation;
import com.itwillbs.ilkwangtech.sales.entity.QuotationDetail;
import com.itwillbs.ilkwangtech.sales.repository.CustomerRepository;
import com.itwillbs.ilkwangtech.sales.repository.ItemStockRepository;
import com.itwillbs.ilkwangtech.sales.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;
    private final ItemStockRepository itemStockRepository;

    @Override
    @Transactional
    public QuotationDTO createQuotation(QuotationDTO quotationDTO) {
        Quotation quotation = new Quotation();
        
        Customer customer = customerRepository.findByCustomerId(quotationDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        quotation.setCustomer(customer);
        quotation.setQuotationNo("QTN-" + System.currentTimeMillis());
        quotation.setQuotationDate(quotationDTO.getQuotationDate());
        quotation.setStatus(QuotationStatus.DRAFT);
        quotation.setSalesPerson(quotationDTO.getSalesPerson());
        quotation.setRemarks(quotationDTO.getRemarks());

        if (quotationDTO.getDetails() != null) {
            List<QuotationDetail> details = quotationDTO.getDetails().stream().map(detailDTO -> {
                QuotationDetail detail = new QuotationDetail();
                detail.setQuotation(quotation);
                detail.setQuantity(detailDTO.getQuantity());
                detail.setUnitPrice(detailDTO.getUnitPrice());
                detail.calculateAmount();
                detail.setRemarks(detailDTO.getRemarks());
                return detail;
            }).collect(Collectors.toList());
            quotation.setDetails(details);
            quotation.calculateTotals();
        }

        Quotation saved = quotationRepository.save(quotation);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuotationDTO getQuotationById(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));
        return convertToDTO(quotation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getAllQuotations() {
        return quotationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuotationDTO updateQuotationStatus(Long id, String status) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));
        quotation.setStatus(QuotationStatus.valueOf(status));
        return convertToDTO(quotationRepository.save(quotation));
    }

    @Override
    @Transactional
    public void deleteQuotation(Long id) {
        quotationRepository.deleteById(id);
    }

    private QuotationDTO convertToDTO(Quotation quotation) {
        QuotationDTO dto = new QuotationDTO();
        dto.setQuotationId(quotation.getQuotationId());
        dto.setQuotationNo(quotation.getQuotationNo());
        dto.setCustomerId(quotation.getCustomer().getCustomerId());
        dto.setCustomerName(quotation.getCustomer().getCustomerName());
        dto.setQuotationDate(quotation.getQuotationDate());
        dto.setStatus(quotation.getStatus());
        dto.setTotalAmount(quotation.getTotalAmount());
        dto.setSalesPerson(quotation.getSalesPerson());
        
        if (quotation.getDetails() != null) {
            dto.setDetails(quotation.getDetails().stream().map(detail -> {
                QuotationDTO.QuotationDetailDTO detailDTO = new QuotationDTO.QuotationDetailDTO();
                detailDTO.setQuotationDetailId(detail.getQuotationDetailId());
                detailDTO.setQuantity(detail.getQuantity());
                detailDTO.setUnitPrice(detail.getUnitPrice());
                detailDTO.setAmount(detail.getAmount());
                detailDTO.setRemarks(detail.getRemarks());
                return detailDTO;
            }).collect(Collectors.toList()));
        }
        
        return dto;
    }
}
