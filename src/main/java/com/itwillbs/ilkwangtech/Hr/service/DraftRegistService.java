package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.entity.DraftRegistEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRegistRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DraftRegistService {


    private final DraftRegistRepository draftRegistRepository;
    private final ModelMapper modelMapper;

    public DraftRegistService(DraftRegistRepository draftRegistRepository, ModelMapper modelMapper) {
        this.draftRegistRepository = draftRegistRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void putDraft(DraftRegistDTO draftRegistDTO) {

        // 결재 목록 테이블
        DraftEntity draftEntity = modelMapper.map(draftRegistDTO, DraftEntity.class);

        // 결재 상태 테이블
        DraftRegistEntity draftRegistEntity = new DraftRegistEntity();
        Member member = new Member();

        draftRegistEntity.setDraftEntity(draftEntity); // 결재 문서 id
        draftRegistEntity.setMember(member); // 사원 고유 id
        draftRegistEntity.setStatus("대기"); // 결재 상태
        draftRegistEntity.setSequence(1L); // 결재 순서

        draftRegistRepository.save(draftRegistEntity);
    }
}
