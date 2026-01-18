package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.entity.DraftRegistEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRegistRepository;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@Service
public class DraftRegistService {

    @PersistenceContext
    private EntityManager entityManager;
    private final DraftRepository draftRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public DraftRegistService(DraftRepository draftRepository, AccountRepository accountRepository, ModelMapper modelMapper) {
        this.draftRepository = draftRepository;
        this.accountRepository = accountRepository;

        this.modelMapper = modelMapper;
    }

    @Transactional
    public void putDraft(DraftRegistDTO draftRegistDTO, Long userId) {

        System.out.println("로그인 아이디 : " + userId);

        // 1. DTO → Entity 기본 필드 매핑
        DraftEntity draftEntity = modelMapper.map(draftRegistDTO, DraftEntity.class);

        // 2. 로그인 사용자(Member)를 FK로 세팅 (SELECT 안 나감)
        Member memberRef = entityManager.getReference(Member.class, userId);
        draftEntity.setMember(memberRef);

        // 3. (선택) 상태값, 기본값 세팅
        draftEntity.setDraftStatus(draftRegistDTO.getDraftStatus());

        // 4. 저장
        draftRepository.save(draftEntity);


//        // 요청 DTO에서 결재자 가져오기
//        List<String> approvers = draftRegistDTO.getDraftApprover();
//
//        // 결재자 순번대로 결재상태 테이블에 저장
//        for(int i = 0; i < approvers.size(); i++) {
//
//            Long approverId = parseLong(approvers.get(i));
//
//        }
    }
}
