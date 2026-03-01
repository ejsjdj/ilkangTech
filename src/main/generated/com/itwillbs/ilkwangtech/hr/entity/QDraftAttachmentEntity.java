package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDraftAttachmentEntity is a Querydsl query type for DraftAttachmentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDraftAttachmentEntity extends EntityPathBase<DraftAttachmentEntity> {

    private static final long serialVersionUID = 294111968L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDraftAttachmentEntity draftAttachmentEntity = new QDraftAttachmentEntity("draftAttachmentEntity");

    public final QDraftEntity draft;

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QDraftAttachmentEntity(String variable) {
        this(DraftAttachmentEntity.class, forVariable(variable), INITS);
    }

    public QDraftAttachmentEntity(Path<? extends DraftAttachmentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDraftAttachmentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDraftAttachmentEntity(PathMetadata metadata, PathInits inits) {
        this(DraftAttachmentEntity.class, metadata, inits);
    }

    public QDraftAttachmentEntity(Class<? extends DraftAttachmentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.draft = inits.isInitialized("draft") ? new QDraftEntity(forProperty("draft"), inits.get("draft")) : null;
    }

}

