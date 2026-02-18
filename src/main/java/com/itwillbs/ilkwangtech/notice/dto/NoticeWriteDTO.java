package com.itwillbs.ilkwangtech.notice.dto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeWriteDTO {
	private Long id; // 수정 시 게시글 ID 식별용
    private String title;
    private String content;
    private boolean isPinned; // 고정 여부
    
    // 파일 처리를 위한 필드
//    private List<MultipartFile> imageFiles; // 이미지 (최대 5개)
    private List<MultipartFile> generalFiles; // 일반 파일 (최대 10개)
    private List<Long> deleteFileIds;
}
