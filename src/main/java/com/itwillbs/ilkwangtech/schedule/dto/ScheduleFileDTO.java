package com.itwillbs.ilkwangtech.schedule.dto;

import com.itwillbs.ilkwangtech.schedule.entity.ScheduleFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleFileDTO {
	
	private Long id;                // 파일 고유 ID (다운로드/삭제 요청 시 사용)
    private String originalFileName; // 화면에 보여줄 원본 파일명
    private String savedFileName;    // 서버에 저장된 실제 파일명 (UUID 포함)
    private Long fileSize;           // 파일 크기 (byte 단위 -> 화면에서 KB/MB 변환)
    
    // [편의 메서드] Entity -> DTO 변환
    public static ScheduleFileDTO fromEntity(ScheduleFile entity) {
        return ScheduleFileDTO.builder()
                .id(entity.getId())
                .originalFileName(entity.getOriginalFileName())
                .savedFileName(entity.getSavedFileName())
                .fileSize(entity.getFileSize())
                .build();
    }

}
