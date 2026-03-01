package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDraftEntity is a Querydsl query type for DraftEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDraftEntity extends EntityPathBase<DraftEntity> {

    private static final long serialVersionUID = 341376093L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDraftEntity draftEntity = new QDraftEntity("draftEntity");

    public final StringPath draftContent = createString("draftContent");

    public final DatePath<java.time.LocalDate> draftEndDate = createDate("draftEndDate", java.time.LocalDate.class);

    public final ListPath<DraftAttachmentEntity, QDraftAttachmentEntity> draftFile = this.<DraftAttachmentEntity, QDraftAttachmentEntity>createList("draftFile", DraftAttachmentEntity.class, QDraftAttachmentEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> draftId = createNumber("draftId", Long.class);

    public final DatePath<java.time.LocalDate> draftStartDate = createDate("draftStartDate", java.time.LocalDate.class);

    public final StringPath draftStatus = createString("draftStatus");

    public final StringPath draftTitle = createString("draftTitle");

    public final NumberPath<Long> draftTotalDate = createNumber("draftTotalDate", Long.class);

    public final StringPath draftType = createString("draftType");

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public QDraftEntity(String variable) {
        this(DraftEntity.class, forVariable(variable), INITS);
    }

    public QDraftEntity(Path<? extends DraftEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDraftEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDraftEntity(PathMetadata metadata, PathInits inits) {
        this(DraftEntity.class, metadata, inits);
    }

    public QDraftEntity(Class<? extends DraftEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

