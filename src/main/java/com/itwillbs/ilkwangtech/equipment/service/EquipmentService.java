package com.itwillbs.ilkwangtech.equipment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.equipment.entity.Equipment;
import com.itwillbs.ilkwangtech.equipment.entity.EquipmentFailure;
import com.itwillbs.ilkwangtech.equipment.repository.EquipmentFailureRepository;
import com.itwillbs.ilkwangtech.equipment.repository.EquipmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentFailureRepository equipmentFailureRepository;

//  모든 설비 리스트 조회
    @Transactional(readOnly = true)
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

//  설비 등록
    public Equipment createEquipment(Equipment equipment) {
        Long id = equipmentRepository.nextId(); // PK 확정
        equipment.setId(id);

        String equipCode = "EQ-" + String.format("%03d", id);
        equipment.setEquipCode(equipCode);

        if (equipment.getUseYn() == null || equipment.getUseYn().isBlank()) {
            equipment.setUseYn("Y"); 
        }

        log.info("equipment-service : " + equipment);
        return equipmentRepository.save(equipment);
    }

//    고장 내역 조회
    @Transactional(readOnly = true)
    public List<EquipmentFailure> getAllFailures() {
    	log.info(">>>>>>>>>>> equipmentFailure-service");
        return equipmentFailureRepository.findAllByOrderByOccurredAtDesc();
        
        // 된건가
    }
    
    
    
}
