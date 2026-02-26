package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructInsertDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionInsturctRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductionInstructServiceImpl implements ProductionInstructService {

    private final ProductionInsturctRepository productionInsturctRepository;


    @Override
    @Transactional
    // 1. 작업지시 목록
    public Page<ProductionInstructDTO> getProductionInstructList(Pageable pageable, String keyword){

        Page<ProductionInstructEntity> productionInstructEntities = productionInsturctRepository.findByKeyword(pageable, keyword);

        return null;
    }

    @Override
    @Transactional
    // 2. 작업지시 상세
    public Optional<ProductionInstructDetailDTO> getProductionInstructDetail(Long instructId){
        return null;
    }

    @Override
    @Transactional
    // 3. 작업지시 등록
    public void saveProductionInstruct(ProductionInstructInsertDTO productionInstructInsertDTO, Long userId){
    }

}