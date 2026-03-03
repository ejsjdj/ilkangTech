package com.itwillbs.ilkwangtech.process.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.process.dto.LotDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.LotResponseDTO;
import com.itwillbs.ilkwangtech.process.entity.LotMaster;
import com.itwillbs.ilkwangtech.process.repository.LotMasterRepository;
import com.itwillbs.ilkwangtech.process.repository.PartProductionRepository;
import com.itwillbs.ilkwangtech.process.repository.QualityCheckRepository;
import com.itwillbs.ilkwangtech.process.repository.RawMaterialRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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

    public List<LotResponseDTO> getAllLotsWithItemName() {
        List<LotMasterRepository.LotSummaryMapping> results = lotMasterRepository.findAllWithItemName();

        log.info(">>>>>>>>>>>>>>>>>> LotTraceService - LOT 상세 리스트 정보 호출");
        return results.stream().map(res -> {
            LotResponseDTO dto = new LotResponseDTO();
            dto.setLotId(res.getLotId());
            dto.setItemName(res.getItemName() != null ? res.getItemName() : "N/A"); //
            dto.setStatus(res.getStatus());
            dto.setCreatedDate(res.getCreatedDate());
            return dto;
        }).collect(Collectors.toList());
    }
    
    public LotDetailResponseDTO getLotDetail(String lotId) {
        LotMasterRepository.LotDetailMapping res = lotMasterRepository.findLotDetailByLotId(lotId);
        if(res == null) return null;

        LotDetailResponseDTO dto = new LotDetailResponseDTO();
        dto.setLotId(res.getLotId());
        dto.setItemName(res.getItemName());
        dto.setInstructCode(res.getInstructCode() != null ? res.getInstructCode() : "-");
        dto.setInstructQty(res.getInstructQty());
        dto.setStatus(res.getStatus());
        dto.setStartDate(res.getStartDate());
        dto.setEndDate(res.getEndDate());
        dto.setDefective(res.getDefective());
        return dto;
    }

	public String getAllProcessStatusList() {
		return null;
	}
}
