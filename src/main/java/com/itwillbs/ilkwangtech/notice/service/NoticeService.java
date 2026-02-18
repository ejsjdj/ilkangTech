package com.itwillbs.ilkwangtech.notice.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
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
    private final NoticeFileRepository noticeFileRepository;
    
    // application.properties의 설정값
    @Value("${file.uploadBaseLocation}") 
    private String baseDir;

    /**
     * [핵심 로직] OS 환경을 감지하여 실제 저장 경로를 반환합니다.
     */
    private String getRealUploadPath() {
        String path = baseDir;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (path.startsWith("/")) {
                path = "C:" + path;
            }
        }
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path;
    }

    public List<NoticeListDTO> getPinnedNotices() {
        return noticeRepository.findTop3ByIsPinnedTrueOrderByRegDateDesc()
                .stream().map(n -> new NoticeListDTO(n.getId(), n.getTitle(), n.getWriterName(), 
                        n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                        n.getViewCount(), n.isPinned(), n.isHasAttachment()))
                .collect(Collectors.toList());
    }

    public Page<NoticeListDTO> getNoticeList(int page, NoticeSearchDTO searchDTO) {
        Pageable pageable = PageRequest.of(page, 10);
        
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (searchDTO.getStartDate() != null) {
            startDateTime = searchDTO.getStartDate().atStartOfDay(); 
        }
        if (searchDTO.getEndDate() != null) {
            endDateTime = searchDTO.getEndDate().atTime(LocalTime.MAX);
        }

        return noticeRepository.searchNotices(
                startDateTime, endDateTime, searchDTO.getSearchType(), 
                searchDTO.getKeyword(), pageable
        ).map(n -> new NoticeListDTO(
                n.getId(), n.getTitle(), n.getWriterName(), 
                n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                n.getViewCount(), n.isPinned(), n.isHasAttachment()));
    }
    
    public boolean checkPinnedLimit() {
        return noticeRepository.countByIsPinnedTrue() >= 3;
    }

    @Transactional
    public void registerNotice(NoticeWriteDTO dto, AccountLogin loginMember) throws IOException {
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writerName(loginMember.getName())
                .writerRank(loginMember.getPosition())
                .writerDept(loginMember.getDepartment())
                .isPinned(dto.isPinned())
                .viewCount(0)
                .hasAttachment(false)
                .build();
        
        Notice savedNotice = noticeRepository.save(notice);
        boolean hasFile = false;

        // [수정된 부분] 폴더 생성 시 OS 경로 반영
        File dir = new File(getRealUploadPath());
        if (!dir.exists()) dir.mkdirs();

//        if (dto.getImageFiles() != null) {
//            for (MultipartFile file : dto.getImageFiles()) {
//                if (!file.isEmpty()) {
//                    saveFile(file, savedNotice, true);
//                    hasFile = true;
//                }
//            }
//        }

        if (dto.getGeneralFiles() != null) {
            for (MultipartFile file : dto.getGeneralFiles()) {
                if (!file.isEmpty()) {
                    saveFile(file, savedNotice, false);
                    hasFile = true;
                }
            }
        }

        if (hasFile) {
            savedNotice.setHasAttachment(true);
        }
    }

    // [수정된 부분] 파일 실제 저장 메소드
    private void saveFile(MultipartFile file, Notice notice, boolean isImage) throws IOException {
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String savedName = uuid + "_" + originalName;
        
        // ★ getRealUploadPath() 사용
        String filePath = getRealUploadPath() + savedName;

        file.transferTo(new File(filePath)); 

        NoticeFile noticeFile = NoticeFile.builder()
                .notice(notice)
                .originalFileName(originalName)
                .savedFileName(savedName)
                .filePath(filePath)
                .fileSize(file.getSize())
                .isImage(isImage)
                .build();

        noticeFileRepository.save(noticeFile); 
    }

    @Transactional
    public NoticeDetailDTO getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        notice.setViewCount(notice.getViewCount() + 1);

        List<NoticeFile> files = noticeFileRepository.findByNoticeId(id);
        List<NoticeDetailDTO.NoticeFileDTO> imageList = new ArrayList<>();
        List<NoticeDetailDTO.NoticeFileDTO> fileList = new ArrayList<>();

        for (NoticeFile file : files) {
            NoticeDetailDTO.NoticeFileDTO fileDTO = new NoticeDetailDTO.NoticeFileDTO(
                    file.getId(), file.getOriginalFileName(), file.getSavedFileName()
            );

            if (file.isImage()) imageList.add(fileDTO);
            else fileList.add(fileDTO);
        }

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
        dto.setImageFiles(imageList);
        dto.setGeneralFiles(fileList);

        return dto;
    }
    
    @Transactional
    public void deleteNotice(Long id) {
        List<NoticeFile> files = noticeFileRepository.findByNoticeId(id);
        for (NoticeFile file : files) {
            // [수정된 부분] 삭제 시 OS 경로 반영
            // DB에 저장된 filePath가 절대경로라면 그대로 쓰고, 아니라면 조합
            // 안전하게 getRealUploadPath() + savedName 으로 접근
            String fullPath = getRealUploadPath() + file.getSavedFileName();
            File localFile = new File(fullPath);
            if (localFile.exists()) localFile.delete();
        }
        
        noticeFileRepository.deleteByNoticeId(id);
        noticeRepository.deleteById(id);
    }

    @Transactional
    public void updateNotice(NoticeWriteDTO dto, AccountLogin loginMember) throws IOException {
        Notice notice = noticeRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setPinned(dto.isPinned());

        if (dto.getDeleteFileIds() != null) {
            for (Long fileId : dto.getDeleteFileIds()) {
                NoticeFile file = noticeFileRepository.findById(fileId).orElse(null);
                if (file != null) {
                    // [수정된 부분] 삭제 시 OS 경로 반영
                    String fullPath = getRealUploadPath() + file.getSavedFileName();
                    File localFile = new File(fullPath);
                    if (localFile.exists()) localFile.delete();
                    
                    noticeFileRepository.delete(file);
                }
            }
        }

        boolean hasNewFile = false;
        
//        if (dto.getImageFiles() != null) {
//            for (MultipartFile file : dto.getImageFiles()) {
//                if (!file.isEmpty()) {
//                    saveFile(file, notice, true);
//                    hasNewFile = true;
//                }
//            }
//        }
        
        if (dto.getGeneralFiles() != null) {
            for (MultipartFile file : dto.getGeneralFiles()) {
                if (!file.isEmpty()) {
                    saveFile(file, notice, false);
                    hasNewFile = true;
                }
            }
        }

        List<NoticeFile> remainingFiles = noticeFileRepository.findByNoticeId(notice.getId());
        notice.setHasAttachment(!remainingFiles.isEmpty());
    }

    public NoticeDetailDTO getNoticeForEdit(Long id) {
        return getNoticeDetail(id);
    }
    
    // [추가] 파일 다운로드를 위한 파일 객체 반환 메서드
    public File getDownloadFile(Long fileId) {
        NoticeFile noticeFile = noticeFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));
        
        // getRealUploadPath()를 사용하여 OS(윈도우/리눅스)에 맞는 정확한 경로 완성
        String realPath = getRealUploadPath() + noticeFile.getSavedFileName();
        
        return new File(realPath);
    }
    
    // [추가] 썸머노트 이미지 업로드 처리
    public String uploadSummernoteImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("파일이 비어있습니다.");
        }

        // 저장 디렉토리 생성
        File dir = new File(getRealUploadPath());
        if (!dir.exists()) dir.mkdirs();

        // UUID 파일명 생성
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String savedName = uuid + "_" + originalName;
        String filePath = getRealUploadPath() + savedName;

        // 파일 저장
        file.transferTo(new File(filePath));

        // 저장된 파일명 반환 (Controller에서 URL 조합용)
        return savedName;
    }

    // [추가] 썸머노트 이미지 조회용 (파일명으로 파일 객체 반환)
    public File getSummernoteImageFile(String filename) {
        String realPath = getRealUploadPath() + filename;
        return new File(realPath);
    }
}