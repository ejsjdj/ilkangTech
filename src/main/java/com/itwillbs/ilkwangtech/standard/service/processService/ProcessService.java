package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.standard.constant.ProcessStatus;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessStatusUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProcessService {

    // 1. 공정코드 리스트 조회
    Page<ProcessDTO> getProcessList(Pageable pageable, String keyword);

    // 2. 공정코드 전체 조회
    List<ProcessDTO> getProcessListAll();

    // 3. 신규 공정코드 등록
    void saveProcessList(ProcessInsertDTO processInsertDTO, Long userId);

    void updateProcessSta(List<ProcessStatusUpdateDTO> requestList);
}