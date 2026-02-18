package com.itwillbs.ilkwangtech.standard.controller.api;


import com.itwillbs.ilkwangtech.standard.dto.ProcessCodeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import com.itwillbs.ilkwangtech.standard.service.processService.ProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoutingApiController {

    private final ProcessService processService;
    // 라우팅 전체 목록 조회
    @GetMapping("/process_mst")
    public Page<ProcessCodeDTO> getProcess(Pageable pageable,
                                           @RequestParam(value = "routeType", required = false) String routeType,
                                           @RequestParam(value = "routeName", required = false) String routeName){

        return processService.getProcessList(pageable);
    }

    // 라우팅 상세 조회
    @GetMapping("/process_mst/detail")
    public List<ProcessDetailDTO> getProcessDetail(@RequestParam(name = "routeId") String routeId){

        return processService.getProcessDetail(routeId);
    }

    // 신규 라우트 추가
    @PostMapping("/process_mst/insert")
    @ResponseBody
    public void insertProcess(@RequestBody ProcessInsertDTO processInsertDTO){
        processService.saveProcess(processInsertDTO);
    }

}
