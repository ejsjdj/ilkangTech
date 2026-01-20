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
    private long draftId;

    @ManyToOne
    @JoinColumn(name = "common_id")
    private Member member;

    @Column(nullable = true)
    private String draftType;

    @Column(nullable = true)
    private String draftTitle;

    @Column(nullable = true)
    private String draftContent;

    @Column(nullable = true)
    private String draftFile;

    @Column(nullable = true)
    private LocalDate draftStartDate;

    @Column(nullable = true)
    private LocalDate draftEndDate;

    @Column(nullable = true)
    private String draftStatus;

}
