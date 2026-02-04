package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.common.entity.FileMeta;
import com.itwillbs.ilkwangtech.common.repository.FileMetaRepository;
import com.itwillbs.ilkwangtech.hr.dto.AttachmentDTO;
import com.itwillbs.ilkwangtech.hr.dto.DraftDetailDTO;
import com.itwillbs.ilkwangtech.hr.entity.DraftAttachmentEntity;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
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

    @Transactional
    public DraftDetailDTO getDraftDetail(Long userId, Long draftId){

        // 문서 상세내용 조회
        DraftEntity draftDetail = draftRepository.findById(draftId).
                orElseThrow(() -> new IllegalArgumentException("문서가 삭제되었거나 존재하지 않습니다"));

        // 2. 해당 문서에 연결된 모든 File ID 추출
        List<Long> fileIds = draftDetail.getDraftFile().stream()
                .map(DraftAttachmentEntity::getFileId)
                .toList();

        // 3. FileMetaRepository에서 한 번에 파일 이름들 조회 (In절 활용)
        // Map<ID, FileName> 형태로 변환하여 매칭하기 쉽게 만듦
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

        return DraftDetailDTO.builder()
                .detailTitle(draftDetail.getDraftTitle())
                .detailContent(draftDetail.getDraftContent())
                .detailFile(files)
                .detailStartDate(draftDetail.getDraftStartDate())
                .detailEndDate(draftDetail.getDraftEndDate())
                .build();
    }
}
