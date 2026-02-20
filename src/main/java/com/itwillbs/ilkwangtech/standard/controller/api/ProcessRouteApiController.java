package com.itwillbs.ilkwangtech.standard.controller.api;


import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteInsertDTO;
import com.itwillbs.ilkwangtech.standard.service.processService.ProcessRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class ProcessRouteApiController {

    private final ProcessRouteService processRouteService;
    // 라우팅 전체 목록 조회
    @GetMapping("/process_mst")
    public Page<ProcessRouteDTO> getProcess(Pageable pageable,
                                            @RequestParam(value = "itemId", required = false) Long itemId,
                                            @RequestParam(value = "routeName", required = false) String routeName){

        log.info("라우팅코드 조회 필터 - 페이지: {}, 드롭다운: {}, 검색어: {}  ", pageable , itemId , routeName);

        Page<ProcessRouteDTO> processCodeDTO = processRouteService.getProcessRouteList(pageable, itemId, routeName);

        log.info("라우팅코드 조회 결과 - 리스트: {}", processCodeDTO.getContent());

        return processCodeDTO;
    }

    // 라우팅 상세 조회
    @GetMapping("/process_mst/detail")
    public List<ProcessRouteDetailDTO> getProcessDetail(@RequestParam(name = "routeId") String routeId){
        return processRouteService.getProcessRouteDetail(routeId);
    }

    // 신규 라우트 추가
    @PostMapping("/process_mst/insert")
    @ResponseBody
    public void insertProcess(@RequestBody List<ProcessRouteInsertDTO> processRouteInsertDTO,
                              @AuthenticationPrincipal AccountLogin accountLogin){

        Long userId = accountLogin.getId();

        processRouteService.saveProcessRoute(processRouteInsertDTO, userId);
    }
}
