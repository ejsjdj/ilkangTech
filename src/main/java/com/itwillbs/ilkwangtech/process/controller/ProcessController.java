package com.itwillbs.ilkwangtech.process.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	    
	    try {
	        ProcessDetailResponseDTO response = lotTraceService.getProcessDetailData(instructCode);
	        
	        // 데이터가 아직 없어서 null이 반환될 경우 빈 객체를 보내 400 에러 방지
	        if (response == null) {
	            log.warn(">>> [" + instructCode + "] 해당 공정의 헤더 데이터가 없습니다. 빈 객체 반환.");
	            return new ProcessDetailResponseDTO(); 
	        }
	        
	        return response;
	        
	    } catch (Exception e) {
	        // 서버에서 터진 '진짜' 에러를 STS 콘솔에 빨간 글씨로 출력
	        log.error(">>> [" + instructCode + "] 상세 데이터 처리 중 서버 에러 발생!!!", e);
	        throw e; 
	    }
	}
	
	@GetMapping("/api/lot/{lotId}/{type}")
	@ResponseBody
	public List<?> getLotSubDetail(
	        @PathVariable("lotId") String lotId,
	        @PathVariable("type") String type  
	    ) {
	    log.info(">>>>>>>>>>>> LOT 서브 상세 호출: " + lotId + ", 타입: " + type);
	    
	    try {
	        if ("FINISHED_USAGE".equalsIgnoreCase(type)) {
	            return lotTraceService.getFinishedUsageList(lotId);
	        } else if ("PROD_PROCESS".equalsIgnoreCase(type) || "processes".equalsIgnoreCase(type)) {
	            // 브라우저 캐시에 예전 주소(processes)가 남아있어도 정상 작동하도록 방어!
	            return lotTraceService.getProcessesByLotId(lotId);
	        } else if ("PROD_MATERIAL".equalsIgnoreCase(type) || "materials".equalsIgnoreCase(type)) {
	            return lotTraceService.getMaterialsByLotId(lotId);
	        }
	    } catch (Exception e) {
	        log.error("서브 상세 데이터 조회 실패", e);
	    }
	    
	    return java.util.Collections.emptyList(); 
	}
	
	@GetMapping("/api/lot/{lotId}/finished_usage") // JS에서 호출하는 경로와 일치해야 함
	@ResponseBody
	public List<Map<String, Object>> getFinishedUsage(@PathVariable("lotId") String lotId) {
	    return lotTraceService.getFinishedUsageList(lotId);
	}
	
	@GetMapping("/api/lot/{lotId}/processes")
	@ResponseBody
	public List<LotMasterRepository.ProdProcessMapping> getLotProcesses(@PathVariable("lotId") String lotId) {
	    return lotTraceService.getProcessesByLotId(lotId);
	}

	@GetMapping("/api/lot/{lotId}/materials")
	@ResponseBody
	public List<LotMasterRepository.FinishedUsageMapping> getLotMaterials(@PathVariable("lotId") String lotId) {
	    return lotTraceService.getMaterialsByLotId(lotId);
	}
	
	@GetMapping("/api/dashboard/defectiveList")
	@ResponseBody
	public List<Map<String, Object>> getDefectiveList() {
	    return lotTraceService.getDefectiveProcessList();
	}
	
	@PostMapping("/api/worker/timeUpdate")
	@ResponseBody
	public ResponseEntity<Map<String, String>> updateWorkerTime(
	        @RequestParam("workerId") Long workerId, 
	        @RequestParam("type") String type) {
	    try {
	        Map<String, String> result = new HashMap<>();
	        if ("START".equalsIgnoreCase(type)) {
	            lotTraceService.startProcessWork(workerId);
	            result.put("status", "Success");
	        } else if ("END".equalsIgnoreCase(type)) {
	            String generatedLotId = lotTraceService.completeProcessWork(workerId);
	            result.put("status", "Success");
	            result.put("lotId", generatedLotId); // 생성된(혹은 공유된) LOT ID 전달
	        }
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).build();
	    }
	}
}
