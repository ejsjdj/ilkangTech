package com.itwillbs.ilkwangtech.common.controller;

import com.itwillbs.ilkwangtech.common.dto.FileUploadDTO;
import com.itwillbs.ilkwangtech.common.entity.FileMeta;
import com.itwillbs.ilkwangtech.common.repository.FileMetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileMetaRepository fileMetaRepository;
    private final String uploadDir = "C:\\uploadFiles";

    @PostMapping("/file/upload")
    public FileUploadDTO upload(@RequestParam MultipartFile file) throws IOException {
        // 1. 서버 디스크에 저장
        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(uploadDir, storedName);
        Files.copy(file.getInputStream(), savePath);

        // 2. 메타정보 DB 저장
        FileMeta meta = new FileMeta();
        meta.setOriginalName(file.getOriginalFilename());
        meta.setStoredName(storedName);
        meta.setFilePath(uploadDir);
        fileMetaRepository.save(meta);

        // 3. fileId 반환
        return new FileUploadDTO(meta.getId());
    }
}
