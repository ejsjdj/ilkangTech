package com.itwillbs.ilkwangtech.schedule.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.schedule.entity.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
	
	private Long id;
	
	private String title;
	private String content; // schedule_memo
	private String type;    // COMPANY, PERSONAL
	
	// HTML <input type="datetime-local"> 대응
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    // 검색용 키워드
    private String keyword;

    // 작성자 정보 (조회 시 표시용)
    private Long writerId;
    private String writerName;
    private String writerDepartment;
    private String writerPosition;
    
 // [편의 메서드] Entity -> DTO 변환
    public static ScheduleDTO fromEntity(Schedule entity) {
        return ScheduleDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .type(entity.getType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .writerId(entity.getWriter().getId())
                .writerName(entity.getWriter().getName())
                .writerDepartment(entity.getWriter().getDepartment())
                .writerPosition(entity.getWriter().getPosition())
                .build();
    }

    // [편의 메서드] DTO -> Entity 변환 (등록용)
    public Schedule toEntity(Member writer) {
        return Schedule.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .type(this.type)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .writer(writer) // 작성자 객체 주입
                .build();
    }

}
