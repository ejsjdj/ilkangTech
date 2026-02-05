package com.itwillbs.ilkwangtech.notice.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeSearchDTO {
    private String searchType;  // 검색 조건 (title, content, writer)
    private String keyword;     // 검색어
    
    private LocalDate startDate; // 조회 시작일
    private LocalDate endDate;   // 조회 종료일
}
