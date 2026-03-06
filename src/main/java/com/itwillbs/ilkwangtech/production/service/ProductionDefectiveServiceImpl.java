package com.itwillbs.ilkwangtech.production.service;

import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import com.itwillbs.ilkwangtech.production.repository.ProductionWorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductionDefectiveServiceImpl implements ProductionDefectiveService {

    private final ProductionWorkerRepository productionWorkerRepository;


    // 불량 수량 등록
    @Override
    @Transactional
    public void instructDefective(Long defectiveQty, Long instructId, Long workerId){
        ProductionWorkerEntity workerEntity = productionWorkerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("작업지시 공정정보가 없습니다."));

        Long productionQty = workerEntity.getProductionQty();
        Long actualQty = productionQty - defectiveQty;

        workerEntity.setDefectiveQty(defectiveQty);
        workerEntity.setActualQty(actualQty);
    }

    // 최종 생산량 개산
    public void finalProductionCal(){

    }

}
