package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDraftApprovalLineEntity is a Querydsl query type for DraftApprovalLineEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDraftApprovalLineEntity extends EntityPathBase<DraftApprovalLineEntity> {

    private static final long serialVersionUID = -80120556L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDraftApprovalLineEntity draftApprovalLineEntity = new QDraftApprovalLineEntity("draftApprovalLineEntity");

    public final StringPath draftType = createString("draftType");

    public final NumberPath<Long> lineId = createNumber("lineId", Long.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final NumberPath<Long> sequence = createNumber("sequence", Long.class);

    public QDraftApprovalLineEntity(String variable) {
        this(DraftApprovalLineEntity.class, forVariable(variable), INITS);
    }

    public QDraftApprovalLineEntity(Path<? extends DraftApprovalLineEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDraftApprovalLineEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDraftApprovalLineEntity(PathMetadata metadata, PathInits inits) {
        this(DraftApprovalLineEntity.class, metadata, inits);
    }

    public QDraftApprovalLineEntity(Class<? extends DraftApprovalLineEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

