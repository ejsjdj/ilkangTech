package com.itwillbs.ilkwangtech.schedule.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "IK_SCHEDULE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_schedule_gen")
	@SequenceGenerator(name = "seq_schedule_gen", sequenceName = "SEQ_SCHEDULE", allocationSize = 1)
	@Column(name = "schedule_id")
	private Long id;
	
	// 작성자 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @Column(name = "schedule_title", nullable = false, length = 200)
    private String title;

    // 공유 범위 (COMPANY, PERSONAL)
    @Column(name = "schedule_type", nullable = false, length = 20)
    private String type;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    // 일정 메모 (CLOB 대응)
    @Lob
    @Column(name = "schedule_memo")
    private String content;

    // 파일 경로
    @Column(name = "attachment_file", length = 200)
    private String attachmentFile;

    // 등록일 자동 생성
    @CreationTimestamp
    @Column(name = "reg_date", nullable = false, updatable = false)
    private LocalDateTime regDate;
    
    // 썸네일(이미지) 경로
    @Column
    private String thumbnailPath; 
    
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleFile> files = new ArrayList<>();

    // 내용 수정 편의 메서드
    public void updateSchedule(String title, String content, String type, 
                               LocalDateTime startDate, LocalDateTime endDate, String attachmentFile, String thumbnailPath) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnailPath = thumbnailPath;
        if(attachmentFile != null) {
            this.attachmentFile = attachmentFile;
        }
    }
    
    // 공유된 부서 목록 (팀 공유 시)
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "schedule_shared_depts",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> sharedDepartments = new HashSet<>();

    // 공유된 사원 목록 (특정 공유 시)
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "schedule_shared_members",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> sharedMembers = new HashSet<>();
    
    // 연관관계 편의 메서드 추가
    public void addSharedDepartment(Department department) {
        this.sharedDepartments.add(department);
    }
    
    public void addSharedMember(Member member) {
        this.sharedMembers.add(member);
    }
    
    public void addFile(ScheduleFile file) {
        this.files.add(file);
        file.setSchedule(this);
    }
    

}
