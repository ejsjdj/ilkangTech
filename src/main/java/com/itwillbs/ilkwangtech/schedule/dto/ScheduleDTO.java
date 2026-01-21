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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
	
	// "팀" "특정" 공유 시 선택된 부서,사원 ID 목록
    private List<Integer> sharedDeptIds;   
    private List<Long> sharedMemberIds;   
    
    // 조회 시 이름을 표시하기 위한 필드
    private List<String> sharedDeptNames;
    private List<String> sharedMemberNames;

    private MultipartFile attachment; // 파일 받는 데이터     
    private String attachmentFile;    // DB에 저장된/저장할 파일 경로 (조회 시 사용)
    private String keyword;           // 검색용 키워드

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
	        builder.sharedDeptIds(entity.getSharedDepartments().stream().map(Department::getId).toList());
            // 이름 리스트 추출
	        builder.sharedDeptNames(entity.getSharedDepartments().stream().map(Department::getDepartmentName).toList());
	    }
	
	    // 공유된 사원 정보가 있다면 ID 추출 (Schedule Entity 업데이트 가정)
		if (entity.getSharedMembers() != null && !entity.getSharedMembers().isEmpty()) {
	        builder.sharedMemberIds(entity.getSharedMembers().stream().map(Member::getId).toList());
            // 이름 리스트 추출
	        builder.sharedMemberNames(entity.getSharedMembers().stream().map(Member::getName).toList());
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
