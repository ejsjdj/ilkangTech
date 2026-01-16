package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.entity.DraftRegistEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRegistRepository;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@Service
public class DraftRegistService {


    private final DraftRegistRepository draftRegistRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public DraftRegistService(DraftRegistRepository draftRegistRepository, AccountRepository accountRepository, ModelMapper modelMapper) {
        this.draftRegistRepository = draftRegistRepository;
        this.accountRepository = accountRepository;

        this.modelMapper = modelMapper;
    }

    @Transactional
    public void putDraft(DraftRegistDTO draftRegistDTO) {



//        // 결재 목록 테이블
//        DraftEntity draftEntity = modelMapper.map(draftRegistDTO, DraftEntity.class);
//
//        // 결재자 가져오기
//        List<String> approvers = draftRegistDTO.getDraftApprover();
//
//        for(int i = 0; i < approvers.size(); i++){
//
//            Long approverId = parseLong(approvers.get(i));
//
//            Optional member = accountRepository.findById(approverId);
//
//            DraftRegistEntity draftRegistEntity = new DraftRegistEntity();
////
////
//            draftRegistEntity.setDraftEntity(draftEntity); // 결재 문서 id
//            draftRegistEntity.setMember(member); // 사원 고유 id
//            draftRegistEntity.setStatus("대기"); // 결재 상태
//            draftRegistEntity.setSequence(1L); // 결재 순서
//        }
    }
}
