package com.itwillbs.ilkwangtech.common.controller;

import com.itwillbs.ilkwangtech.common.dto.FileUploadDTO;
import com.itwillbs.ilkwangtech.common.entity.FileMeta;
import com.itwillbs.ilkwangtech.common.repository.FileMetaRepository;
import com.itwillbs.ilkwangtech.common.service.FlieDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileMetaRepository fileMetaRepository;
    private final FlieDownloadService fileDownloadService;

    @Value("${file.uploadBaseLocation:/upload/}")
    private String uploadBaseLocation;

    @PostMapping("/file/upload")
    public FileUploadDTO upload(@RequestParam MultipartFile file) throws IOException {
        // 1. 서버 디스크에 저장
        Path uploadPath = Paths.get(uploadBaseLocation);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path savePath = uploadPath.resolve(storedName);

        Files.copy(file.getInputStream(), savePath);

        // 2. 메타정보 DB 저장
        FileMeta meta = new FileMeta();
        meta.setOriginalName(file.getOriginalFilename());
        meta.setStoredName(storedName);
        meta.setFilePath(uploadBaseLocation);
        FileMeta savedMeta = fileMetaRepository.saveAndFlush(meta);

        System.out.println("FileId = " + savedMeta.getId());

        // 3. fileId 반환
        return new FileUploadDTO(savedMeta.getId());
    }

    // 파일 다운로드
    @GetMapping("/file/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) throws IOException {

        return fileDownloadService.fileDownload(fileId);
    }

    // 이미지 스트리밍 (URL 기반)
    @GetMapping("/display")
    public ResponseEntity<Resource> display(@RequestParam("fileName") String fileName) {
        String fullPath = uploadBaseLocation + fileName;
        Resource resource = new FileSystemResource(fullPath);

        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders header = new HttpHeaders();
        try {
            Path filePath = Paths.get(fullPath);
            header.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }
}
