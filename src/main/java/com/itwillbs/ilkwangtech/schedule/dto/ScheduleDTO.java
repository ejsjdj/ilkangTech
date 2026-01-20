package com.itwillbs.ilkwangtech.schedule.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.ilkwangtech.account.entity.Department;
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
	
	// "팀" 공유 시 선택된 부서 ID 목록
    private List<Integer> sharedDeptIds;   
    
    // "특정" 공유 시 선택된 사원 ID 목록
    private List<Long> sharedMemberIds;    

    // HTML Form의 <input type="file">에서 받는 실제 파일 데이터
    private MultipartFile attachment;      
    
    // DB에 저장된/저장할 파일 경로 (조회 시 사용)
    private String attachmentFile;

    // 검색용 키워드
    private String keyword;

    // 작성자 정보 (조회 시 표시용)
    private Long writerId;
    private String writerName;
    private String writerDepartment;
    private String writerPosition;
    
    // [편의 메서드] Entity -> DTO 변환
    public static ScheduleDTO fromEntity(Schedule entity) {
		ScheduleDTOBuilder builder = ScheduleDTO.builder()
	            .id(entity.getId())
	            .title(entity.getTitle())
	            .content(entity.getContent())
	            .type(entity.getType())
	            .startDate(entity.getStartDate())
	            .endDate(entity.getEndDate())
	            .attachmentFile(entity.getAttachmentFile()) // 파일 경로 매핑
	            .writerId(entity.getWriter().getId())
	            .writerName(entity.getWriter().getName())
	            .writerDepartment(String.valueOf(entity.getWriter().getDepartment()))
	            .writerPosition(String.valueOf(entity.getWriter().getPosition()));
	
	    // 공유된 부서 정보가 있다면 ID 추출 (Schedule Entity 업데이트 가정)
	    if (entity.getSharedDepartments() != null && !entity.getSharedDepartments().isEmpty()) {
	        builder.sharedDeptIds(entity.getSharedDepartments().stream()
	                .map(Department::getId)
	                .toList());
	    }
	
	    // 공유된 사원 정보가 있다면 ID 추출 (Schedule Entity 업데이트 가정)
	    if (entity.getSharedMembers() != null && !entity.getSharedMembers().isEmpty()) {
	        builder.sharedMemberIds(entity.getSharedMembers().stream()
	                .map(Member::getId)
	                .toList());
	    }
	
	    return builder.build();
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
                .attachmentFile(this.attachmentFile)
                .writer(writer) // 작성자 객체 주입
                .build();
    }

}
