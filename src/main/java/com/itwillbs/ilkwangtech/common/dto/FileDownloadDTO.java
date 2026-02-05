package com.itwillbs.ilkwangtech.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDownloadDTO {

    private String filePath;
    private String storedName;

    @Builder
    public FileDownloadDTO(String filePath, String storedName){
        this.filePath = filePath;
        this.storedName = storedName;
    }

}