package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.entity.DraftRegistEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRegistRepository;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
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
    private final DraftRegistRepository draftRegistRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public DraftRegistService(DraftRepository draftRepository, DraftRegistRepository draftRegistRepository, ModelMapper modelMapper, MemberRepository memberRepository) {
        this.draftRepository = draftRepository;
        this.draftRegistRepository = draftRegistRepository;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void putDraft(DraftRegistDTO draftRegistDTO, Long userId) {

        // Draft 엔티티에 새로문 결재문서 등록
        DraftEntity draftEntity = modelMapper.map(draftRegistDTO, DraftEntity.class); // DTO와 Entity 매핑
        Member memberRef = entityManager.getReference(Member.class, userId); // member 엔티티에서 작성자 ID 참조
        draftEntity.setMember(memberRef); // 작성자 ID 등록
        draftEntity.setDraftStatus(draftRegistDTO.getDraftStatus()); // 최종 결재상태(기본값 WAT) 등록
        DraftEntity savedDraft = draftRepository.save(draftEntity);

        // 결재자 리스트
        List<String> approvers = draftRegistDTO.getDraftApprover();

        // 리스트 가공
        for(String approverString : approvers){

            // null 체크
            if (approverString == null || approverString.trim().isEmpty()) {
                break;
            }

            DraftRegistEntity draftRegistEntity = new DraftRegistEntity();

            String[] parts = approverString.trim().split("\\s+");
            Long memberId = Long.parseLong(parts[0]); // 사원 ID 추출
            int sequence = Integer.parseInt(parts[parts.length - 1]); // 결재 순서 추출
            Member approver = entityManager.getReference(Member.class, memberId); // member 엔티티에서 결재자 ID 참조

            draftRegistEntity.setDraftEntity(savedDraft);// 결재문서 ID 등록
            draftRegistEntity.setMember(approver); // 결재 지정자 등록
            draftRegistEntity.setSequence(sequence); // 결재 순서 등록
            draftRegistEntity.setStatus("WAT"); // 결재 상태 등록
            draftRegistRepository.save(draftRegistEntity);
        }

    }
}
