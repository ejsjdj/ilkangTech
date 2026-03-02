package com.itwillbs.ilkwangtech.process.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.ilkwangtech.process.entity.LotMaster;
import com.itwillbs.ilkwangtech.process.service.LotTraceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/process")
@RequiredArgsConstructor
public class ProcessController {

	private final LotTraceService lotTraceService;
	
	@GetMapping("/LOT")
    public String chaseLOTPage() {
        return "process/LOT"; 
    }
	
	// LOT 리스트 데이터 반환
    @GetMapping("/api/lots")
    @ResponseBody
    public List<LotMaster> getLotList() {
    	log.info(">>>>>>>>>>>>>>>> ProcessController - api/lots 호출");
        return lotTraceService.getAllLots();
    }

    // LOT 상세 데이터 반환
    @GetMapping("/api/lot/{lotId}")
    @ResponseBody
    public Map<String, Object> getLotDetail(@PathVariable String lotId) {
        return lotTraceService.getLotDetail(lotId);
    }
}
