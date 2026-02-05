package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.common.entity.FileMeta;
import com.itwillbs.ilkwangtech.common.repository.FileMetaRepository;
import com.itwillbs.ilkwangtech.hr.dto.AttachmentDTO;
import com.itwillbs.ilkwangtech.hr.dto.DraftDetailDTO;
import com.itwillbs.ilkwangtech.hr.entity.DraftApproveStatusEntity;
import com.itwillbs.ilkwangtech.hr.entity.DraftAttachmentEntity;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DraftDetailService {

    private final DraftRepository draftRepository;
    private final FileMetaRepository fileMetaRepository;
    private final DraftApproveStatusRepository draftApproveStatusRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @Transactional
    public DraftDetailDTO getDraftDetail(Long userId, Long draftId, List<String> roleList){

        // 0. 부서 및 직급 조회
        Map<Integer, String> deptMap = departmentRepository.findAll().stream()
                .collect(Collectors.toMap(Department::getId, Department::getDepartmentName, (e, r) -> e));

        Map<Integer, String> positionMap = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getPositionName, (e, r) -> e));

        // 1. 문서 상세내용 조회
        DraftEntity draftDetail = draftRepository.findById(draftId).
                orElseThrow(() -> new IllegalArgumentException("문서가 삭제되었거나 존재하지 않습니다"));

        // 2. 해당 문서에 연결된 모든 File ID 추출
        List<Long> fileIds = draftDetail.getDraftFile().stream()
                .map(DraftAttachmentEntity::getFileId)
                .toList();

        // 3. 파일 이름 조회
        Map<Long, String> fileMetaMap = fileMetaRepository.findAllById(fileIds).stream()
                .collect(Collectors.toMap(FileMeta::getId, FileMeta::getOriginalName));

        // 4. DTO 조립
        List<AttachmentDTO> files = draftDetail.getDraftFile().stream()
                .map(e -> {
                    AttachmentDTO dto = new AttachmentDTO();
                    dto.setFileId(e.getFileId());
                    dto.setFileName(fileMetaMap.getOrDefault(e.getFileId(), "Unknown_File"));
                    return dto;
                }).toList();

        // 5. 해당 문서에 대한 승인/반려 상황 조회
        List<DraftApproveStatusEntity> approvalStatusList = draftApproveStatusRepository.findByDraftEntity_DraftId(draftId);


        List<DraftDetailDTO.ApprovalInfo> approvalInfo = approvalStatusList.stream()
                .map(entity -> {
                    Member m = entity.getMember();

                    String deptName = deptMap.getOrDefault(m.getDepartment(), "소속없음");
                    String posName = positionMap.getOrDefault(m.getPosition(), "직급없음");

                    return DraftDetailDTO.ApprovalInfo.builder()
                            .name(m.getName())
                            .department(deptName)
                            .position(posName)
                            .status(entity.getStatus())
                            .sequence(entity.getSequence())
                            .build();
                }).collect(Collectors.toList());


        return DraftDetailDTO.builder()
                .userId(userId)
                .detailWriterId(draftDetail.getMember().getId())
                .detailRoles(roleList)
                .detailTitle(draftDetail.getDraftTitle())
                .detailContent(draftDetail.getDraftContent())
                .detailFile(files)
                .detailStartDate(draftDetail.getDraftStartDate())
                .detailEndDate(draftDetail.getDraftEndDate())
                .approvalLine(approvalInfo)
                .build();
    }
}

//TODO ::: 기안자인지 확인하는 서버 로직 추가할것
//  if(draftDetail.getMember().getId().equals(userId)){
//      throw new IllegalArgumentException("기안자 본인은 결재를 처리할 수 없습니다.");
//  }