package com.itwillbs.ilkwangtech.hr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DraftDetailDTO {
    private Long userId;
    private Long detailWriterId;
    private List<String> detailRoles;
    private String detailTitle;
    private String detailContent;
    private List<AttachmentDTO> detailFile;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate detailStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate detailEndDate;

    // 결재 상황
    private List<ApprovalInfo> approvalLine;

    @Getter
    @Setter
    @Builder
    public static class ApprovalInfo {
        private String name;
        private String department;
        private String position;
        private String status;
        private Long sequence;
    }

    @Builder
    public DraftDetailDTO(Long userId, Long detailWriterId, List<String> detailRoles, String detailTitle, String detailContent, List<AttachmentDTO> detailFile, LocalDate detailStartDate, LocalDate detailEndDate, List<ApprovalInfo> approvalLine){
        this.userId = userId;
        this.detailWriterId = detailWriterId;
        this.detailRoles = detailRoles;
        this.detailTitle = detailTitle;
        this.detailContent = detailContent;
        this.detailFile = detailFile;
        this.detailStartDate = detailStartDate;
        this.detailEndDate = detailEndDate;
        this.approvalLine = approvalLine;
    }
}