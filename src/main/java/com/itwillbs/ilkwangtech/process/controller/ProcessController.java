package com.itwillbs.ilkwangtech.process.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.ilkwangtech.process.dto.LotDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.LotResponseDTO;
import com.itwillbs.ilkwangtech.process.service.LotTraceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/process")
@RequiredArgsConstructor
public class ProcessController {

	private final LotTraceService lotTraceService;
	
	@GetMapping("/ProcessDashboard")
    public String processDashboardPage() {
        return "process/ProcessDashboard"; 
    }
	
	@GetMapping("/ProcessStatus")
    public String processStatusPage() {
        return "process/ProcessStatus"; 
    }
	
	@GetMapping("/api/processStatusList")
	@ResponseBody
    public String getProcessStatusList() {
        return lotTraceService.getAllProcessStatusList(); 
    }
	
	@GetMapping("/LOT")
    public String chaseLOTPage() {
        return "process/LOT"; 
    }
	
	// LOT 리스트 데이터 반환
	@GetMapping("/api/lots")
	@ResponseBody
	public List<LotResponseDTO> getLotList() { 
	    return lotTraceService.getAllLotsWithItemName(); 
	}

    // LOT 상세 데이터 반환
	@GetMapping("/api/lot/{lotId}")
	@ResponseBody
	public LotDetailResponseDTO getLotDetail(@PathVariable String lotId) {
	    return lotTraceService.getLotDetail(lotId);
	}
}
