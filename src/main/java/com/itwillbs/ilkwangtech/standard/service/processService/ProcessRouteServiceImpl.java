package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteInsertDTO;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import com.itwillbs.ilkwangtech.standard.repository.OperationRepository;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


// 공정라우팅 리스트
@Service
@RequiredArgsConstructor
public class ProcessRouteServiceImpl implements ProcessRouteService {

    private final ProcessRouteRepository processRouteRepository;
    private final OperationRepository operationRepository;
    private final MemberRepository memberRepository;

    // 1. 라우트 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ProcessRouteDTO> getProcessRouteList(Pageable pageable, Long itemId, String routeName){

        Page<ProcessRouteEntity> processEntities =
                processRouteRepository.findDistinctRouteIdBy(itemId, routeName, pageable);

        return processEntities.map(processRouteEntity -> ProcessRouteDTO.builder()
                .routeId(processRouteEntity.getRouteId())
                .itemId(processRouteEntity.getItemId())
                .routeName(processRouteEntity.getRouteName())
                .description(processRouteEntity.getDescription())
                .createdAt(String.valueOf(processRouteEntity.getCreatedAt()))
                .constructor(processRouteEntity.getMember().getName())
                .build());
    }

    // 2. 라우트 상세 조회
    @Override
    @Transactional
    public List<ProcessRouteDetailDTO> getProcessRouteDetail(String routeId){
        return processRouteRepository.findByRouteId(routeId);
    }

    // 3. 신규 라우트 등록
    @Override
    @Transactional
    public void saveProcessRoute(List<ProcessRouteInsertDTO> processRouteInsertDTO, Long userId){

        for(ProcessRouteInsertDTO saveDTO : processRouteInsertDTO) {
            // 1. 공정 ID 찾기
            ProcessEntity processEntity = operationRepository.findById(saveDTO.getOperationId())
                    .orElseThrow(() -> new IllegalArgumentException("공정정보가 없습니다."));

            // 2. 등록자 ID 찾기
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

            // 3. 엔티티에 등록
            ProcessRouteEntity process = ProcessRouteEntity.builder().
                    id(saveDTO.getId()).
                    routeId(saveDTO.getRouteId()).
                    operation(processEntity).
                    itemId(saveDTO.getItemId()).
                    sequence(saveDTO.getSequence()).
                    routeName(saveDTO.getRouteName()).
                    description(saveDTO.getDescription()).
                    note(saveDTO.getNote()).
                    createdAt(LocalDate.now()).
                    member(member).
                    build();

            // 4. save
            processRouteRepository.save(process);
        }
    }
}