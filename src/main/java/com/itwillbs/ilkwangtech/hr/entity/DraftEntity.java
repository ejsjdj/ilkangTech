package com.itwillbs.ilkwangtech.hr.entity;

import com.itwillbs.ilkwangtech.hr.dto.AttachmentDTO;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "draft_document")
@SequenceGenerator(
        name = "draft_document_seq_gen",
        sequenceName = "draft_document_seq",
        initialValue = 100,
        allocationSize = 1
)
public class DraftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_document_seq_gen")
    private long draftId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = true)
    private String draftType;

    @Column(nullable = true)
    private String draftTitle;

    @Column(nullable = true)
    private String draftContent;

    @OneToMany(mappedBy = "draft", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DraftAttachmentEntity> draftFile;

    @Column(nullable = true)
    private LocalDate draftStartDate;

    @Column(nullable = true)
    private LocalDate draftEndDate;

    @Column(nullable = true)
    private String draftStatus;

    @Column(nullable = true)
    private Long draftTotalDate;

}
