package com.itwillbs.ilkwangtech.standard.service.processService;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDetailDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteInsertDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessUpdateDTO;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import com.itwillbs.ilkwangtech.standard.repository.OperationRepository;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRepository;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// 공정라우팅 리스트
@Service
@RequiredArgsConstructor
public class ProcessRouteServiceImpl implements ProcessRouteService {

    private final ProcessRouteRepository processRouteRepository;
    private final ProcessRepository processRepository;
    private final OperationRepository operationRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 1. 라우트 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ProcessRouteDTO> getProcessRouteList(Pageable pageable, Long itemId, String routeName){

        Page<ProcessRouteEntity> processEntities =
                processRouteRepository.findDistinctRouteIdBy(pageable);

        return processEntities.map(processRouteEntity -> ProcessRouteDTO.builder()
                .routeCode(processRouteEntity.getRouteCode())
                .itemName(processRouteEntity.getItem().getItemName())
                .routeName(processRouteEntity.getRouteName())
                .description(processRouteEntity.getDescription())
                .createdAt(processRouteEntity.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .constructor(processRouteEntity.getMember().getName())
                .build());
    }

    // 2. 라우트 상세 조회
    @Override
    @Transactional
    public List<ProcessRouteDetailDTO> getProcessRouteDetail(String routeId){
        List<ProcessRouteEntity> entities = processRouteRepository.findByRouteCodeOrderBySequenceAsc(routeId);

        return entities.stream()
                .map(processRouteEntity -> ProcessRouteDetailDTO.builder()
                        .id(processRouteEntity.getId())
                        .operationId(processRouteEntity.getOperation().getId())
                        .operationCode(processRouteEntity.getOperation().getOperationCode())
                        .name(processRouteEntity.getOperation().getName())
                        .description(processRouteEntity.getOperation().getDescription())
                        .sequence(processRouteEntity.getSequence())
                        .note(processRouteEntity.getNote())
                .build())
                .toList();
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

            ItemEntity item = itemRepository.findById(saveDTO.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("품목정보가 없습니다."));

            // 3. 엔티티에 등록
            ProcessRouteEntity process = ProcessRouteEntity.builder().
                    routeCode(saveDTO.getRouteCode()).
                    operation(processEntity).
                    item(item).
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

    // 4. 라우트 업데이트
    @Override
    @Transactional
    public void updateProcessList(List<ProcessUpdateDTO> dtoList,
                                  Long userId,
                                  String routeCode) {

        Member member = memberRepository.findById(userId)
                .orElseThrow();

        // 기존 라우트 단계 조회
        List<ProcessRouteEntity> existingList =
                processRouteRepository.findByRouteCodeOrderBySequenceAsc(routeCode);

        Map<Long, ProcessRouteEntity> existingMap =
                existingList.stream()
                        .collect(Collectors.toMap(ProcessRouteEntity::getId, e -> e));

        for (ProcessUpdateDTO dto : dtoList) {

            if (dto.getId() != null) {
                // =========================
                // 1. 기존 단계 수정
                // =========================
                ProcessRouteEntity entity = existingMap.get(dto.getId());

                if (entity != null) {
                    entity.update(
                            dto.getSequence(),
                            dto.getNote(),
                            member
                    );

                    existingMap.remove(dto.getId());
                }

            } else {
                // =========================
                // 2. 신규 단계 추가
                // =========================
                ProcessEntity operation = processRepository
                        .findById(dto.getOperationId())
                        .orElseThrow();

                ProcessRouteEntity newEntity = ProcessRouteEntity.builder()
                        .routeCode(routeCode)
                        .operation(operation)
                        .item(existingList.get(0).getItem()) // 기존 라우트 기준 유지
                        .routeName(existingList.get(0).getRouteName())
                        .sequence(dto.getSequence())
                        .description(dto.getDescription())
                        .note(dto.getNote())
                        .createdAt(LocalDate.now())
                        .member(member)
                        .build();

                processRouteRepository.save(newEntity);
            }
        }

        // =========================
        // 3. 삭제 처리
        // =========================
        if (!existingMap.isEmpty()) {
            processRouteRepository.deleteAll(existingMap.values());
        }
    }
}