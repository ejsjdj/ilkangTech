package com.itwillbs.ilkwangtech.process.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.ilkwangtech.process.dto.LotResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessDetailResponseDTO;
import com.itwillbs.ilkwangtech.process.dto.ProcessStatusResponseDTO;
import com.itwillbs.ilkwangtech.process.repository.LotMasterRepository;
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
    public List<ProcessStatusResponseDTO> getProcessStatusList() {
		log.info(">>>>>>>>>>>> processStatusList 리스트 호출");
        return lotTraceService.getAllProcessStatusList(); 
    }
	
	@GetMapping("/api/lot/prodSubDetail/{lotId}")
	@ResponseBody
	public LotMasterRepository.ProdSubDetailMapping getProdSubDetail(@PathVariable("lotId") String lotId) {
	    return lotTraceService.getProdSubDetailInfo(lotId);
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
	public Object getLotDetail(@PathVariable("lotId") String lotId) {
	    if (lotId.startsWith("RW-")) {
	        return lotTraceService.getRawMaterialDetail(lotId);
	    }
	    return lotTraceService.getLotDetail(lotId);
	}
	
	@GetMapping("/api/lot/subDetail/{lotId}")
	@ResponseBody
	public LotMasterRepository.SubDetailMapping getUsageSubDetail(@PathVariable("lotId") String lotId) {
	    return lotTraceService.getSubDetailInfo(lotId);
	}
	
	@GetMapping("/api/processDetail/{instructCode}")
	@ResponseBody
	public ProcessDetailResponseDTO getProcessDetail(@PathVariable("instructCode") String instructCode) {
	    log.info(">>>>>>>>>>>> 상세 정보 호출: " + instructCode);
	    return lotTraceService.getProcessDetailData(instructCode);
	}
	
	@GetMapping("/api/lot/{lotId}/{type}")
	@ResponseBody
	public List<?> getLotSubDetail(
	        @PathVariable("lotId") String lotId,
	        @PathVariable("type") String type  
	    ) {
	    log.info(">>>>>>>>>>>> LOT 서브 상세 호출: " + lotId + ", 타입: " + type);
	    
	    if ("FINISHED_USAGE".equalsIgnoreCase(type)) {
	        return lotTraceService.getFinishedUsageList(lotId);
	    }
	    
	    return null; 
	}
	
	@GetMapping("/api/lot/{lotId}/finished_usage") // JS에서 호출하는 경로와 일치해야 함
	@ResponseBody
	public List<Map<String, Object>> getFinishedUsage(@PathVariable("lotId") String lotId) {
	    return lotTraceService.getFinishedUsageList(lotId);
	}
	
	
}
