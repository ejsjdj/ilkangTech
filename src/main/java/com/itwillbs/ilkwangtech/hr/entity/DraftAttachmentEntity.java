package com.itwillbs.ilkwangtech.hr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "draft_attachment_entity")
@SequenceGenerator(
        name = "draft_attach_seq_gen",
        sequenceName = "draft_attach_seq",
        initialValue = 1,
        allocationSize = 1
)
public class DraftAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_attach_seq_gen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draft_draft_id", nullable = false)
    private DraftEntity draft;

    @Column(nullable = false)
    private Long fileId;

}
