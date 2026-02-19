package com.itwillbs.ilkwangtech.standard.controller.api;


import com.itwillbs.ilkwangtech.standard.dto.ProcessCodeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import com.itwillbs.ilkwangtech.standard.service.processService.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class RoutingApiController {

    private final ProcessService processService;
    // 라우팅 전체 목록 조회
    @GetMapping("/process_mst")
    public Page<ProcessCodeDTO> getProcess(Pageable pageable,
                                           @RequestParam(value = "itemId", required = false) Long itemId,
                                           @RequestParam(value = "routeName", required = false) String routeName){

        log.info("라우팅코드 조회 필터 - 페이지: {}, 드롭다운: {}, 검색키워드: {}  ", pageable , itemId , routeName);

        Page<ProcessCodeDTO> processCodeDTO = processService.getProcessList(pageable, itemId, routeName);

        log.info("조회된 공정 라우트 리스트 - 내용: {}", processCodeDTO.getContent());

        return processCodeDTO;
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
