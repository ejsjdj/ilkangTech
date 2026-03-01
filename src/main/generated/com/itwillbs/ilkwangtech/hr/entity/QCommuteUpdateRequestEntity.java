package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommuteUpdateRequestEntity is a Querydsl query type for CommuteUpdateRequestEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommuteUpdateRequestEntity extends EntityPathBase<CommuteUpdateRequestEntity> {

    private static final long serialVersionUID = -1157172490L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommuteUpdateRequestEntity commuteUpdateRequestEntity = new QCommuteUpdateRequestEntity("commuteUpdateRequestEntity");

    public final DatePath<java.time.LocalDate> allowDate = createDate("allowDate", java.time.LocalDate.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember approver;

    public final NumberPath<Long> attendance = createNumber("attendance", Long.class);

    public final StringPath context = createString("context");

    public final StringPath contextDetail = createString("contextDetail");

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final DatePath<java.time.LocalDate> targetDate = createDate("targetDate", java.time.LocalDate.class);

    public QCommuteUpdateRequestEntity(String variable) {
        this(CommuteUpdateRequestEntity.class, forVariable(variable), INITS);
    }

    public QCommuteUpdateRequestEntity(Path<? extends CommuteUpdateRequestEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommuteUpdateRequestEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommuteUpdateRequestEntity(PathMetadata metadata, PathInits inits) {
        this(CommuteUpdateRequestEntity.class, metadata, inits);
    }

    public QCommuteUpdateRequestEntity(Class<? extends CommuteUpdateRequestEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.approver = inits.isInitialized("approver") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("approver")) : null;
    }

}

