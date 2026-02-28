package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDraftApproveStatusEntity is a Querydsl query type for DraftApproveStatusEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDraftApproveStatusEntity extends EntityPathBase<DraftApproveStatusEntity> {

    private static final long serialVersionUID = 970861608L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDraftApproveStatusEntity draftApproveStatusEntity = new QDraftApproveStatusEntity("draftApproveStatusEntity");

    public final QDraftEntity draftEntity;

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final NumberPath<Long> sequence = createNumber("sequence", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> statusId = createNumber("statusId", Long.class);

    public QDraftApproveStatusEntity(String variable) {
        this(DraftApproveStatusEntity.class, forVariable(variable), INITS);
    }

    public QDraftApproveStatusEntity(Path<? extends DraftApproveStatusEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDraftApproveStatusEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDraftApproveStatusEntity(PathMetadata metadata, PathInits inits) {
        this(DraftApproveStatusEntity.class, metadata, inits);
    }

    public QDraftApproveStatusEntity(Class<? extends DraftApproveStatusEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.draftEntity = inits.isInitialized("draftEntity") ? new QDraftEntity(forProperty("draftEntity"), inits.get("draftEntity")) : null;
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

