package com.itwillbs.ilkwangtech.notice.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.itwillbs.ilkwangtech.notice.dto.NoticeSearchDTO;
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
	
	// 파일 저장 경로
    private final String UPLOAD_DIR = "C:/upload/";

    public List<NoticeListDTO> getPinnedNotices() {
        return noticeRepository.findTop3ByIsPinnedTrueOrderByRegDateDesc()
                .stream().map(n -> new NoticeListDTO(n.getId(), n.getTitle(), n.getWriterName(), 
                        n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                        n.getViewCount(), n.isPinned(), n.isHasAttachment()))
                .collect(Collectors.toList());
    }

    // 검색 조건을 포함한 리스트 조회
    public Page<NoticeListDTO> getNoticeList(int page, NoticeSearchDTO searchDTO) {
        Pageable pageable = PageRequest.of(page, 10);
        
        // 날짜 변환 (LocalDate -> LocalDateTime)
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (searchDTO.getStartDate() != null) {
            startDateTime = searchDTO.getStartDate().atStartOfDay(); // 00:00:00
        }
        if (searchDTO.getEndDate() != null) {
            endDateTime = searchDTO.getEndDate().atTime(LocalTime.MAX); // 23:59:59.999...
        }

        // 레포지토리 호출
        return noticeRepository.searchNotices(
                startDateTime, 
                endDateTime, 
                searchDTO.getSearchType(), 
                searchDTO.getKeyword(), 
                pageable
        ).map(n -> new NoticeListDTO(
                n.getId(), n.getTitle(), n.getWriterName(), 
                n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                n.getViewCount(), n.isPinned(), n.isHasAttachment()));
    }
    
    // 현재 고정 게시글 개수 확인
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

    // 2. 상세 조회
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
                // 이미지라면 imageList에 담기
                imageList.add(fileDTO);
            } else {
                // 일반 파일이라면 fileList에 담기
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
        dto.setPinned(notice.isPinned());
        
        // 분리한 리스트 주입
        dto.setImageFiles(imageList);
        dto.setGeneralFiles(fileList);

        return dto;
    }
    
    // 게시글 삭제 (파일 포함)
    @Transactional
    public void deleteNotice(Long id) {
        // 1. 파일 삭제 (디스크 + DB)
        List<NoticeFile> files = noticeFileRepository.findByNoticeId(id);
        for (NoticeFile file : files) {
            File localFile = new File(file.getFilePath());
            if (localFile.exists()) localFile.delete();
        }
        
        // DB에서 파일 데이터 삭제 (Cascade 설정이 없으므로 수동 삭제)
        noticeFileRepository.deleteByNoticeId(id);

        // 2. 게시글 삭제
        noticeRepository.deleteById(id);
    }

    // 게시글 수정
    @Transactional
    public void updateNotice(NoticeWriteDTO dto, AccountLogin loginMember) throws IOException {
        Notice notice = noticeRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 1. 기본 정보 수정 (Dirty Checking)
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setPinned(dto.isPinned());

        // 2. 삭제 요청된 기존 파일 삭제
        if (dto.getDeleteFileIds() != null) {
            for (Long fileId : dto.getDeleteFileIds()) {
                NoticeFile file = noticeFileRepository.findById(fileId).orElse(null);
                if (file != null) {
                    // 실제 파일 삭제
                    File localFile = new File(file.getFilePath());
                    if (localFile.exists()) localFile.delete();
                    
                    // DB 삭제
                    noticeFileRepository.delete(file);
                }
            }
        }

        // 3. 새로운 파일 추가 (기존 register 로직 활용)
        boolean hasNewFile = false;
        
        // (1) 새 이미지 저장
        if (dto.getImageFiles() != null) {
            for (MultipartFile file : dto.getImageFiles()) {
                if (!file.isEmpty()) {
                    saveFile(file, notice, true);
                    hasNewFile = true;
                }
            }
        }
        // (2) 새 일반 파일 저장
        if (dto.getGeneralFiles() != null) {
            for (MultipartFile file : dto.getGeneralFiles()) {
                if (!file.isEmpty()) {
                    saveFile(file, notice, false);
                    hasNewFile = true;
                }
            }
        }

        // 4. 첨부파일 여부(hasAttachment) 갱신
        // 기존 파일이 남아있거나, 새로 추가된 파일이 있으면 true
        List<NoticeFile> remainingFiles = noticeFileRepository.findByNoticeId(notice.getId());
        notice.setHasAttachment(!remainingFiles.isEmpty());
    }

    // 파일 디스크 삭제 헬퍼 메서드
    private void deleteFileFromDisk(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
    
    // 수정 폼용 데이터 조회
    public NoticeDetailDTO getNoticeForEdit(Long id) {
        return getNoticeDetail(id); // 기존 상세 조회 로직 활용 (조회수 증가 로직이 포함되어 있다면 분리 고려)
    }

}
