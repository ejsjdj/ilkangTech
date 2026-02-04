package com.itwillbs.ilkwangtech.notice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id") // Notice 테이블의 PK를 참조
    private Notice notice;

    private String originalFileName; // 사용자가 올린 원본 파일명
    private String savedFileName;    // 서버에 저장된 유니크한 파일명 (UUID 포함)
    private String filePath;         // 전체 경로
    private long fileSize;
    
    private boolean isImage; // 이미지 여부 (true면 본문 하단에 이미지로 표시)
}
