package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.standard.dto.ProcessCodeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import com.itwillbs.ilkwangtech.standard.entity.OperationEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.repository.OperationRepository;
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
    private final OperationRepository operationRepository;
    private final MemberRepository memberRepository;

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
    public void saveProcess(List<ProcessInsertDTO> processInsertDTO, Long userId){

        for(ProcessInsertDTO saveDTO : processInsertDTO) {
            OperationEntity operationEntity = operationRepository.findById(saveDTO.getOperationId())
                    .orElseThrow(() -> new IllegalArgumentException("공정정보가 없습니다."));

            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 존재하지 않습니다. 재로그인 해주세요"));


            // TODO ::: 생성일 필드 추가할 것 !!!!!
            new ProcessEntity(
                    saveDTO.getId(),
                    saveDTO.getRouteId(),
                    operationEntity,
                    saveDTO.getItemId(),
                    saveDTO.getSequence(),
                    saveDTO.getRouteName(),
                    saveDTO.getDescription(),
                    saveDTO.getNote(),
                    member
            );

        }
    }
}