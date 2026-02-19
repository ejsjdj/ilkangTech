package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.standard.dto.ProcessCodeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProcessService {

    // 1. 라우트 전체 조회
    Page<ProcessCodeDTO> getProcessList(Pageable pageable, String routeType, String routeName);

    // 2. 라우트 상세 조회
    List<ProcessDetailDTO> getProcessDetail(String routeId);

    // 3. 라우트 등록
    void saveProcess(ProcessInsertDTO processInsertDTO);
}
