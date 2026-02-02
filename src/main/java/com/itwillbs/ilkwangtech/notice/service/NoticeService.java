package com.itwillbs.ilkwangtech.notice.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.notice.dto.NoticeDetailDTO;
import com.itwillbs.ilkwangtech.notice.dto.NoticeListDTO;
import com.itwillbs.ilkwangtech.notice.dto.NoticeWriteDTO;
import com.itwillbs.ilkwangtech.notice.entity.Notice;
import com.itwillbs.ilkwangtech.notice.entity.NoticeFile;
import com.itwillbs.ilkwangtech.notice.repository.NoticeFileRepository;
import com.itwillbs.ilkwangtech.notice.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
	
	private final NoticeRepository noticeRepository;
	private final NoticeFileRepository noticeFileRepository; // 추가 필요
	
	// 파일 저장 경로 (실제 존재하는 폴더여야 함)
    private final String UPLOAD_DIR = "C:/upload/";

    public List<NoticeListDTO> getPinnedNotices() {
        return noticeRepository.findTop3ByIsPinnedTrueOrderByRegDateDesc()
                .stream().map(n -> new NoticeListDTO(n.getId(), n.getTitle(), n.getWriterName(), 
                        n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                        n.getViewCount(), n.isPinned(), n.isHasAttachment()))
                .collect(Collectors.toList());
    }

    public Page<NoticeListDTO> getNoticeList(int page) {
        // 1-3. 페이지당 10개씩
        Pageable pageable = PageRequest.of(page, 10);
        return noticeRepository.findByIsPinnedFalseOrderByRegDateDesc(pageable)
                .map(n -> new NoticeListDTO(n.getId(), n.getTitle(), n.getWriterName(), 
                        n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                        n.getViewCount(), n.isPinned(), n.isHasAttachment()));
    }
    
    // 1-6. 현재 고정 게시글 개수 확인
    public boolean checkPinnedLimit() {
        return noticeRepository.countByIsPinnedTrue() >= 3;
    }

    @Transactional
    public void registerNotice(NoticeWriteDTO dto, AccountLogin loginMember) throws IOException {
    	// Notice 엔티티 저장
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writerName(loginMember.getName())
                .writerRank(loginMember.getPosition())
                .writerDept(loginMember.getDepartment())
                .isPinned(dto.isPinned())
                .viewCount(0)
                .hasAttachment(false) // 일단 false, 파일 있으면 true로 변경
                .build();
        
        Notice savedNotice = noticeRepository.save(notice);
        boolean hasFile = false;

        // 폴더가 없으면 생성
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        // (1) 이미지 파일 저장
        if (dto.getImageFiles() != null) {
            for (MultipartFile file : dto.getImageFiles()) {
                if (!file.isEmpty()) {
                    saveFile(file, savedNotice, true);
                    hasFile = true;
                }
            }
        }

        // (2) 일반 파일 저장
        if (dto.getGeneralFiles() != null) {
            for (MultipartFile file : dto.getGeneralFiles()) {
                if (!file.isEmpty()) {
                    saveFile(file, savedNotice, false);
                    hasFile = true;
                }
            }
        }

        // 첨부파일 여부 업데이트
        if (hasFile) {
            savedNotice.setHasAttachment(true);
        }
    }

    // 파일 실제 저장 메소드
    private void saveFile(MultipartFile file, Notice notice, boolean isImage) throws IOException {
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String savedName = uuid + "_" + originalName;
        String filePath = UPLOAD_DIR + savedName;

        file.transferTo(new File(filePath)); // 디스크에 저장

        NoticeFile noticeFile = NoticeFile.builder()
                .notice(notice)
                .originalFileName(originalName)
                .savedFileName(savedName)
                .filePath(filePath)
                .fileSize(file.getSize())
                .isImage(isImage)
                .build();

        noticeFileRepository.save(noticeFile); // DB에 저장
    }

    // 2. 상세 조회 (DTO 변환)
    @Transactional
    public NoticeDetailDTO getNoticeDetail(Long id) {
    	Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 조회수 증가
        notice.setViewCount(notice.getViewCount() + 1);

        // 1. 해당 게시글의 모든 파일 조회
        List<NoticeFile> files = noticeFileRepository.findByNoticeId(id);

        // 2. 이미지와 일반 파일 리스트 초기화
        List<NoticeDetailDTO.NoticeFileDTO> imageList = new ArrayList<>();
        List<NoticeDetailDTO.NoticeFileDTO> fileList = new ArrayList<>();

        // 3. 파일 종류에 따라 리스트 분리 (이 부분이 핵심!)
        for (NoticeFile file : files) {
            NoticeDetailDTO.NoticeFileDTO fileDTO = new NoticeDetailDTO.NoticeFileDTO(
                    file.getId(),
                    file.getOriginalFileName(),
                    file.getSavedFileName()
            );

            if (file.isImage()) {
                // 이미지라면 imageList에 담기 -> <img> 태그로 보여짐
                imageList.add(fileDTO);
            } else {
                // 일반 파일이라면 fileList에 담기 -> 다운로드 링크로 보여짐
                fileList.add(fileDTO);
            }
        }

        // 4. DTO 생성 및 데이터 주입
        NoticeDetailDTO dto = new NoticeDetailDTO();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setWriterName(notice.getWriterName());
        dto.setWriterRank(notice.getWriterRank());
        dto.setWriterDept(notice.getWriterDept());
        dto.setViewCount(notice.getViewCount());
        dto.setRegDate(notice.getRegDate());
        
        // 분리한 리스트 주입
        dto.setImageFiles(imageList);
        dto.setGeneralFiles(fileList);

        return dto;
    }

}
