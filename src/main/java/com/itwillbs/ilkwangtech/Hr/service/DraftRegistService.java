package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.Hr.entity.DraftApproveStatusEntity;
import com.itwillbs.ilkwangtech.Hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.Hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Long.parseLong;

@Service
public class DraftRegistService {

    @PersistenceContext
    private EntityManager entityManager;
    private final DraftRepository draftRepository;
    private final DraftApproveStatusRepository draftApproveStatusRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public DraftRegistService(DraftRepository draftRepository, DraftApproveStatusRepository draftApproveStatusRepository, ModelMapper modelMapper, MemberRepository memberRepository) {
        this.draftRepository = draftRepository;
        this.draftApproveStatusRepository = draftApproveStatusRepository;
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

            DraftApproveStatusEntity draftApproveStatusEntity = new DraftApproveStatusEntity();

            String[] parts = approverString.trim().split("\\s+");
            Long memberId = Long.parseLong(parts[0]); // 사원 ID 추출
            int sequence = Integer.parseInt(parts[parts.length - 1]); // 결재 순서 추출
            Member approver = entityManager.getReference(Member.class, memberId); // member 엔티티에서 결재자 ID 참조

            draftApproveStatusEntity.setDraftEntity(savedDraft);// 결재문서 ID 등록
            draftApproveStatusEntity.setMember(approver); // 결재 지정자 등록
            draftApproveStatusEntity.setSequence(sequence); // 결재 순서 등록
            draftApproveStatusEntity.setStatus("대기"); // 결재 상태 등록
            draftApproveStatusRepository.save(draftApproveStatusEntity);
        }

    }
}
