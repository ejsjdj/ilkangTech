package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.AppointmentRegistDTO;
import com.itwillbs.ilkwangtech.hr.dto.DraftRegistDTO;
import com.itwillbs.ilkwangtech.hr.entity.DraftAttachmentEntity;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.hr.entity.DraftApproveStatusEntity;
import com.itwillbs.ilkwangtech.hr.repository.DraftApprovalLineRepository;
import com.itwillbs.ilkwangtech.hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Long.parseLong;

@Service
@AllArgsConstructor
public class DraftRegistService {

    @PersistenceContext
    private EntityManager entityManager;
    private final DraftRepository draftRepository;
    private final DraftApproveStatusRepository draftApproveStatusRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final DraftApprovalLineRepository draftApprovalLineRepository;
    private final RegistAppointmentService registAppointmentService;

    @Transactional
    public void putDraft(DraftRegistDTO draftRegistDTO, Long userId) {

        List<DraftAttachmentEntity> attachment = null;

        // Draft 엔티티에 새로문 결재문서 등록
        DraftEntity draftEntity = modelMapper.map(draftRegistDTO, DraftEntity.class); // DTO와 Entity 매핑
        draftEntity.setMember(entityManager.getReference(Member.class, userId)); // 작성자 ID 등록

        if (draftRegistDTO.getDraftFile() != null) {
            attachment = draftRegistDTO.getDraftFile().stream()
                    .map(attachmentDTO -> {
                        DraftAttachmentEntity e = new DraftAttachmentEntity();
                        e.setFileId(attachmentDTO.getFileId());
                        e.setDraft(draftEntity);
                        return e;
                    }).toList();
            draftEntity.setDraftFile(attachment);
            draftEntity.setDraftStatus(draftRegistDTO.getDraftStatus()); // 최종 결재상태(기본값 WAT) 등록

            DraftEntity savedDraft = draftRepository.save(draftEntity);

            if ("APP".equals(draftRegistDTO.getDraftType())) {
                registAppointmentService.registAppointment(draftRegistDTO, savedDraft);
            }

            // 결재자 리스트
            List<String> approverStrings;

            if (draftRegistDTO.getDraftApprover() == null
                    || draftRegistDTO.getDraftApprover().isEmpty()) {

                // 기본 결재선 조회
                approverStrings = draftApprovalLineRepository
                        .findByDraftType("APP")
                        .stream()
                        .map(line ->
                                line.getMember().getId() + " " + line.getSequence()
                        )
                        .toList();

            } else {
                approverStrings = draftRegistDTO.getDraftApprover();
            }

            // 리스트 가공
            for (String approverString : approverStrings) {

                if (approverString == null || approverString.trim().isEmpty()) {
                    continue;
                }

                DraftApproveStatusEntity entity = new DraftApproveStatusEntity();

                String[] parts = approverString.trim().split("\\s+");
                Long memberId = Long.parseLong(parts[0]);
                int sequence = Integer.parseInt(parts[parts.length - 1]);

                Member approver = entityManager.getReference(Member.class, memberId);

                entity.setDraftEntity(savedDraft);
                entity.setMember(approver);
                entity.setSequence(sequence);
                entity.setStatus("대기");

                draftApproveStatusRepository.save(entity);
            }
        }
    }
}
