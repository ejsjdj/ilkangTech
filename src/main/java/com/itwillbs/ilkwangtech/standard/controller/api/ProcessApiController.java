package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import com.itwillbs.ilkwangtech.standard.service.processService.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class ProcessApiController {

    private final ProcessService processService;

    // 1. 공정코드 리스트 조회
    @GetMapping("/process_code")
    public Page<ProcessDTO> getProcess(Pageable pageable,
                                       @RequestParam(name = "processId") Long processId,
                                       @RequestParam(name = "processName") String processName){

        log.info("공정코드 조회 필터 - 페이지 : {}, 드롭다운 : {}, 검색어 : {}  ", pageable, processId, processName);

        Page<ProcessDTO> processDTO = processService.getProcessList(pageable, processId, processName);

        log.info("공정코드 조회 결과 - 리스트 : {}", processDTO.getContent());

        return processDTO;

    }

    // 2. 신규 공정코드 추가
    @PostMapping("/insert_process_code")
    public void insertProcess(List<ProcessInsertDTO> processInsertDTO,
                              @AuthenticationPrincipal AccountLogin accountLogin){

        log.info("신규 공정코드 등록 - 리스트 : {}, 로그인 유저 : {} ", processInsertDTO, accountLogin.getId());

        Long userId = accountLogin.getId();

        processService.saveProcessList(processInsertDTO, userId);

    }


}