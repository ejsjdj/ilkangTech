package com.itwillbs.ilkwangtech.equipment.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.ilkwangtech.equipment.entity.Equipment;
import com.itwillbs.ilkwangtech.equipment.entity.EqFailure;
import com.itwillbs.ilkwangtech.equipment.entity.EqHistory;
import com.itwillbs.ilkwangtech.equipment.service.EqService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/equipment")
@RequiredArgsConstructor
@Log4j2
public class EqController {

    private final EqService eqService;

//  설비 현황 페이지 접속
    @GetMapping("/equipmentList")
    public String equipmentPage() {
        return "equipment/equipmentList"; 
    }

//  TOAST UI Grid가 호출할 데이터 API
    @GetMapping("/api/equipment")
    @ResponseBody
    public List<Equipment> getEquipmentApi() {
        return eqService.getAllEquipments();
    }
    
//  새 설비 등록
    @PostMapping("/api/equipment")
    @ResponseBody
    public Equipment create(@RequestBody Equipment equipment) {
    	log.info(">>>>>>>>>>> equipment-controller : " + equipment);
        return eqService.createEquipment(equipment);
    }
    
//  설비 작동 이력 페이지 접속
    @GetMapping("/equipmentHistory")
    public String equipmentHistoryPage() {
        return "equipment/equipmentHistory"; 
    }
    
//  설비 가동 이력 데이터 반환
    // URL: http://localhost:포트번호/equipment/api/history
    @GetMapping("/api/history")
    @ResponseBody
    public List<EqHistory> getHistoryList() {
        return eqService.findAllHistory();
    }
    
//  고장 이력 페이지 접속
    @GetMapping("/failure-history")
    public String failureHistoryPage() {
    	log.info(">>>>>>>>>>>>>>>>> failure-history 접속");
        return "equipment/equipmentFailureList"; 
    }
    
//  고장 이력 데이터 반환
    @GetMapping("/equipmentFailureList")
    @ResponseBody
    public List<EqFailure> getFailureList() {
    	log.info(">>>>>>>>>>>>>>>>> failure 리스트 controller 접속");
        return eqService.getAllFailures();
    }
}
