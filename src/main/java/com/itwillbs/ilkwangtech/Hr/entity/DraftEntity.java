package com.itwillbs.ilkwangtech.Hr.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "draft_document")
public class DraftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long draft_id;

    @ManyToOne
    @JoinColumn(name = "common_id")
    private Member member;

    @Column(nullable = true)
    private String draft_type;

    @Column(nullable = true)
    private String draft_title;

    @Column(nullable = true)
    private String draft_content;

    @Column(nullable = true)
    private String draft_file;

    @Column(nullable = true)
    private LocalDate draft_startDate;

    @Column(nullable = true)
    private LocalDate draft_endDate;

    @Column(nullable = true)
    private String draft_status;

}
