package com.itwillbs.ilkwangtech.standard.service.processService;


import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.standard.constant.ProcessStatus;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessStatusUpdateDTO;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import com.itwillbs.ilkwangtech.standard.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService{

    private final ProcessRepository processRepository;
    private final MemberRepository memberRepository;

    // 1. 공정코드 리스트 조회
    @Override
    @Transactional
    public Page<ProcessDTO> getProcessList(Pageable pageable, String keyword){

        Page<ProcessEntity> processEntities = processRepository.findByKeyword(keyword, pageable);

        return processEntities.map(processEntity -> ProcessDTO.builder().
                id(processEntity.getId()).
                operationCode(processEntity.getOperationCode()).
                name(processEntity.getName()).
                description(processEntity.getDescription()).
                memberName(processEntity.getMember().getName()).
                createdAt(String.valueOf(processEntity.getCreatedAt())).
                status(processEntity.getStatus()).
                build());
    }

    // 2. 공정코드 전체 조회
    @Override
    @Transactional
    public List<ProcessDTO> getProcessListAll(){
        List<ProcessEntity> entity = processRepository.findAll();

        return entity.stream().map(processEntity -> ProcessDTO.builder().
                id(processEntity.getId()).
                operationCode(processEntity.getOperationCode()).
                name(processEntity.getName()).
                description(processEntity.getDescription()).
                memberName(processEntity.getMember().getName()).
                createdAt(String.valueOf(processEntity.getCreatedAt())).
                build()).toList();
    }

    // 3. 신규 공정코드 등록
    @Override
    @Transactional
    public void saveProcessList(ProcessInsertDTO processInsertDTO, Long userId){

            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

            ProcessEntity process = ProcessEntity.builder().
                    operationCode(processInsertDTO.getOperationCode()).
                    name(processInsertDTO.getName()).
                    description(processInsertDTO.getDescription()).
                    member(member).
                    createdAt(LocalDate.now()).
                    status(ProcessStatus.ACTIVE).
                    build();

            processRepository.save(process);

    }

    @Override
    @Transactional
    public void updateProcessSta(List<ProcessStatusUpdateDTO> requestList) {

        for (ProcessStatusUpdateDTO dto : requestList) {

            ProcessEntity entity = processRepository.findById(dto.getProcessId())
                    .orElseThrow(() -> new IllegalArgumentException("공정을 찾을 수 없습니다."));

                entity.setOperationCode(dto.getOperationCode());
                entity.setName(dto.getName());
                entity.setDescription(dto.getDescription());
                entity.setStatus(dto.getStatus());

        }
    }
}