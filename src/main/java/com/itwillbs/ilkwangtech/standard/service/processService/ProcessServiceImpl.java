package com.itwillbs.ilkwangtech.standard.service.processService;


import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessInsertDTO;
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

    private ProcessRepository processRepository;
    private MemberRepository memberRepository;

    // 1. 공정코드 리스트 조회
    @Override
    @Transactional
    public Page<ProcessDTO> getProcessList(Pageable pageable, Long processId, String processName){

        String nameParam = (processName != null) ? "%" + processName + "%" : null;

        Page<ProcessEntity> processEntities = processRepository.findByProcessId(processId, nameParam, pageable);

        return processEntities.map(processEntity -> ProcessDTO.builder().
                opertaionId(processEntity.getOperationId()).
                name(processEntity.getName()).
                description(processEntity.getDescription()).
                memberName(processEntity.getMember().getName()).
                createdAt(String.valueOf(processEntity.getCreatedAt())).
                build());
    }

    // 2. 신규 공정코드 등록
    @Override
    @Transactional
    public void saveProcessList(List<ProcessInsertDTO> processInsertDTO, Long userId){
        for(ProcessInsertDTO saveDTO : processInsertDTO){

            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("등록자 정보가 없습니다. 재로그인 해주세요"));

            ProcessEntity process = ProcessEntity.builder().
                    operationId(saveDTO.getOperationId()).
                    name(saveDTO.getName()).
                    description(saveDTO.getDescription()).
                    member(member).
                    createdAt(LocalDate.now()).
                    build();

            processRepository.save(process);
        }
    }
}