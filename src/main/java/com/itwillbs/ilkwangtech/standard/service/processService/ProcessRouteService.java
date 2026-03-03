package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteInsertDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProcessRouteService {

    // 1. 라우트 전체 조회
    Page<ProcessRouteDTO> getProcessRouteList(Pageable pageable, Long itemId, String routeName);

    // 2. 라우트 상세 조회
    List<ProcessRouteDetailDTO> getProcessRouteDetail(String routeId);

    // 3. 라우트 등록
    void saveProcessRoute(List<ProcessRouteInsertDTO> processRouteInsertDTO, Long userId);

    // 4. 라우트 업데이트
    void updateProcessList(List<ProcessUpdateDTO> processUpdateDTO, Long userId, String routeCode);
}
