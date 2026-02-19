package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.standard.dto.ProcessCodeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 공정라우팅 리스트
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;

    // 1. 라우트 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ProcessCodeDTO> getProcessList(Pageable pageable, Long itemId, String routeName){

        Page<ProcessEntity> processEntities =
                processRepository.findDistinctRouteIdBy(itemId, routeName, pageable);

        return processEntities.map(processEntity -> ProcessCodeDTO.builder()
                .routeId(processEntity.getRouteId())
                .itemId(processEntity.getItemId())
                .routeName(processEntity.getRouteName())
                .description(processEntity.getDescription())
                .createdAt(processEntity.getCreatedAt())
                .constructor(processEntity.getMember().getName())
                .build());
    }

    // 2. 라우트 상세 조회
    @Override
    @Transactional
    public List<ProcessDetailDTO> getProcessDetail(String routeId){
        return processRepository.findByRouteId(routeId);
    }

    // 3. 신규 라우트 등록
    @Override
    @Transactional
    public void saveProcess(ProcessInsertDTO processInsertDTO){
        // TODO ::: 라우트 등록 기능 구현
    }
}