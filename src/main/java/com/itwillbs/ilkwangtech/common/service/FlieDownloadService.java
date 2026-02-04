package com.itwillbs.ilkwangtech.common.service;

import com.itwillbs.ilkwangtech.common.entity.FileMeta;
import com.itwillbs.ilkwangtech.common.repository.FileMetaRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class FlieDownloadService {

    private final FileMetaRepository fileMetaRepository;

    public ResponseEntity<Resource> fileDownload(Long fileId) throws IOException {

        FileMeta fileMeta = fileMetaRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일이 존재하지 않습니다."));

        Path filePath = Paths.get(
                fileMeta.getFilePath(),
                fileMeta.getStoredName()
        );

        Resource resource = new UrlResource(filePath.toUri());

        if(!resource.exists()){
            throw new RuntimeException("파일이 존재하지 않습니다.");
        }
        String encodedFileName = URLEncoder.encode(fileMeta.getOriginalName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        // 3. 응답 생성 (헤더가 핵심!)
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // 이진 데이터임
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);

    }
}
