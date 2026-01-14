package com.itwillbs.ilkwangtech.schedule.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.itwillbs.ilkwangtech.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
	
	// 작성자 (FK) - 지연 로딩 권장
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

    // 내용 수정 편의 메서드
    public void updateSchedule(String title, String content, String type, 
                               LocalDateTime startDate, LocalDateTime endDate, String attachmentFile) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        if(attachmentFile != null) {
            this.attachmentFile = attachmentFile;
        }
    }
    
    

}
