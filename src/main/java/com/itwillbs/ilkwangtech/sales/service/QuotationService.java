package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.QuotationDTO;
import java.util.List;

public interface QuotationService {
    QuotationDTO createQuotation(QuotationDTO quotationDTO);
    QuotationDTO getQuotationById(Long id);
    List<QuotationDTO> getAllQuotations();
    QuotationDTO updateQuotationStatus(Long id, String status);
    void deleteQuotation(Long id);
}
