package com.itwillbs.ilkwangtech.notice.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class NoticeListDTO {
	
	private Long id;
    private String title;
    private String writerInfo; // 이름(직급) 형태
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int viewCount;
    private boolean isPinned;
    private boolean hasAttachment;

    public NoticeListDTO(Long id, String title, String name, String rank, 
                         LocalDateTime regDate, LocalDateTime modDate, 
                         int viewCount, boolean isPinned, boolean hasAttachment) {
        this.id = id;
        this.title = title;
        this.writerInfo = name + "(" + rank + ")";
        this.regDate = regDate;
        this.modDate = modDate;
        this.viewCount = viewCount;
        this.isPinned = isPinned;
        this.hasAttachment = hasAttachment;
    }

}
