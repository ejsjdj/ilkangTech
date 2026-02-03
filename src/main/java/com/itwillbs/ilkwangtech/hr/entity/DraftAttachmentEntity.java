package com.itwillbs.ilkwangtech.hr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DraftAttachmentEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private DraftEntity draft;

    @Column(nullable = false)
    private Long fileId;

}
