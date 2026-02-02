package com.itwillbs.ilkwangtech.hr.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="commute_update_request")
@NoArgsConstructor
public class CommuteUpdateRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_approval_line_seq_gen")
    private Long requestId;

//    @OneToOne
//    @JoinColumn(name = "attendance_id")
    @Column(name = "attendance_id", nullable = true)
    private Long attendance;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = true)
    private Member approver;

    @Column(name = "allow_type", nullable = true)
    private String allowType;

    @Column(name = "allow_context", nullable = true)
    private String context;

    @Column(name = "allow_context_detail", nullable = true)
    private String contextDetail;

    @Column(name = "allow_file", nullable = true)
    private String file;

    @Column(name = "allow_status", nullable = true)
    private String allowStatus;

    @Column(name = "allow_regist_date", nullable = true)
    private LocalDate registDate;

    @Column(name = "allow_date", nullable = true)
    private LocalDate allowDate;


//    public void setRequestData(Member requester, Attendance attendanceId, String allowType, String context, String contextDetail, String file){
//        this.requester = requester;
//        this.attendance = attendanceId;
//        this.allowType = allowType;
//        this.context = context;
//        this.contextDetail = contextDetail;
//        this.file = file;
//    }

}
