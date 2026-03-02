package com.itwillbs.ilkwangtech.process.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.process.entity.LotMaster;
import com.itwillbs.ilkwangtech.process.repository.LotMasterRepository;
import com.itwillbs.ilkwangtech.process.repository.PartProductionRepository;
import com.itwillbs.ilkwangtech.process.repository.QualityCheckRepository;
import com.itwillbs.ilkwangtech.process.repository.RawMaterialRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LotTraceService {
    private final LotMasterRepository lotMasterRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final PartProductionRepository partProductionRepository;
    private final QualityCheckRepository qualityCheckRepository;

    // 1. 좌측 리스트용 전체 LOT 조회
    public List<LotMaster> getAllLots() {
        return lotMasterRepository.findAll();
    }

    // 2. 우측 상세창용 통합 데이터 조회
    public Map<String, Object> getLotDetail(String lotId) {
        Map<String, Object> details = new HashMap<>();
        
        details.put("master", lotMasterRepository.findById(lotId).orElse(null));
        details.put("materials", rawMaterialRepository.findByLotMaster_LotId(lotId));
        details.put("productions", partProductionRepository.findByLotMaster_LotId(lotId));
        details.put("quality", qualityCheckRepository.findByLotMaster_LotId(lotId));
        
        return details;
    }
}
