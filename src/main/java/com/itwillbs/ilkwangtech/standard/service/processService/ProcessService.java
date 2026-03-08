package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.standard.dto.ProcessDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProcessService {

    // 1. 공정코드 리스트 조회
    Page<ProcessDTO> getProcessList(Pageable pageable, Long processId, String processName);


    // 2. 신규 공정코드 등록
    void saveProcessList(List<ProcessInsertDTO> processInsertDTO, Long userId);
}