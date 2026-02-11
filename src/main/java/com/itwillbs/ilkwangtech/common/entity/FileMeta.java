package com.itwillbs.ilkwangtech.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FileMeta {

    @Id
    @GeneratedValue
    private Long id;

    private String originalName; // 사용자가 올린 이름
    private String storedName;   // 서버에 저장된 이름
    private String filePath;     // 디렉토리 경로

    private Long fileSize;
    private String contentType;

    private LocalDateTime uploadedAt;
}
