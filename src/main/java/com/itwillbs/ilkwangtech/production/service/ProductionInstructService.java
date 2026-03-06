package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductionInstructService {

    Page<ProductionInstructDTO> getProductionInstructList(Pageable pageable, String keyword);

    Optional<ProductionInstructDetailDTO> getProductionInstructDetail(Long instructId);

    void saveProductionInstruct(ProductionInstructInsertDTO productionInstructInsertDTO, Long userId);

    void startInstruct(Long planeId, Long instructId, Long workerId);

    void completeInstruct(Long planeId, Long instructId, Long workerId);

    void cancelInstruct(Long planeId, Long instructId, Long workerId);


}
