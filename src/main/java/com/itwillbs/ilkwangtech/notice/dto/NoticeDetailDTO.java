package com.itwillbs.ilkwangtech.notice.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDetailDTO {
    private Long id;
    private String title;
    private String content;
    private String writerName;
    private String writerRank;
    private String writerDept;
    private int viewCount;
    private LocalDateTime regDate;
    
    // [추가] 고정 공지 여부 필드 추가
    private boolean isPinned;
    
    // 분리된 파일 리스트
    private List<NoticeFileDTO> imageFiles;   // 이미지 파일들
    private List<NoticeFileDTO> generalFiles; // 일반 첨부 파일들
    
    // 파일 정보 내부 클래스
    @Getter @Setter
    public static class NoticeFileDTO {
        private Long id;
        private String originalFileName;
        private String savedFileName;
        
        public NoticeFileDTO(Long id, String originalFileName, String savedFileName) {
            this.id = id;
            this.originalFileName = originalFileName;
            this.savedFileName = savedFileName;
        }
    }
}
