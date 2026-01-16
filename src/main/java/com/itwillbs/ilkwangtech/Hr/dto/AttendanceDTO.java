package com.itwillbs.ilkwangtech.Hr.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.itwillbs.ilkwangtech.Hr.constant.AttendanceStatus;
import com.itwillbs.ilkwangtech.Hr.entity.Attendance;
import com.itwillbs.ilkwangtech.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
	
private Long id;
    
    private Long memberId;
    private String memberName;
    private String memberPosition;
    
    private LocalDate workDate;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    
    private AttendanceStatus status; // 화면에서 'OUT_WORK' 또는 'LEAVE'를 여기로 바로 받음
    private String statusKor;
    
    // [수정] category 삭제됨
    private String memo; // 사유는 여기에 작성
    
    private String message; 

    // Entity -> DTO
    public static AttendanceDTO fromEntity(Attendance entity) {
        return AttendanceDTO.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .memberName(entity.getMember().getName())
                .memberPosition(entity.getMember().getPosition())
                .workDate(entity.getWorkDate())
                .inTime(entity.getInTime())
                .outTime(entity.getOutTime())
                .status(entity.getStatus())
                .statusKor(entity.getStatus().getDescription()) // "외근", "장기휴식" 등 한글명
                .memo(entity.getMemo())
                .build();
    }
    
    // DTO -> Entity
    public Attendance toEntity(Member member) {
        return Attendance.builder()
                .id(this.id)
                .member(member)
                .workDate(this.workDate)
                .inTime(this.inTime)
                .outTime(this.outTime)
                .status(this.status)
                .memo(this.memo)
                .build();
    }

}
